package com.innovatech.mstareas.service;

import com.innovatech.mstareas.dto.ActualizarEstadoTareaRequest;
import com.innovatech.mstareas.dto.TareaKpiDTO;
import com.innovatech.mstareas.model.EstadoTarea;
import com.innovatech.mstareas.model.PrioridadTarea;
import com.innovatech.mstareas.model.Tarea;
import com.innovatech.mstareas.repository.TareaRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

// Implementacion real de la logica de negocio del microservicio de tareas.
@Service
public class TareaServiceImpl implements TareaService {

    private final TareaRepository tareaRepository;

    public TareaServiceImpl(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Override
    public List<Tarea> obtenerTodas() {
        return tareaRepository.findAll();
    }

    @Override
    public Optional<Tarea> obtenerPorId(Long id) {
        return tareaRepository.findById(id);
    }

    @Override
    public List<Tarea> obtenerPorProyecto(Long proyectoId) {
        return tareaRepository.findByProyectoId(proyectoId);
    }

    @Override
    public List<Tarea> obtenerPorResponsable(Long responsableId) {
        return tareaRepository.findByResponsableId(responsableId);
    }

    @Override
    public TareaKpiDTO obtenerKpis() {
        // Los KPIs se calculan en ms-tareas porque este servicio es el dueno
        // de los datos de tareas. El BFF solo consume este resumen.
        List<Tarea> tareas = tareaRepository.findAll();
        int total = tareas.size();

        TareaKpiDTO kpis = new TareaKpiDTO();
        kpis.setTotalTareas(total);
        kpis.setTareasPendientes(contarPorEstado(tareas, EstadoTarea.PENDIENTE));
        kpis.setTareasEnProgreso(contarPorEstado(tareas, EstadoTarea.EN_PROGRESO));
        kpis.setTareasCompletadas(contarPorEstado(tareas, EstadoTarea.COMPLETADA));
        kpis.setTareasBloqueadas(contarPorEstado(tareas, EstadoTarea.BLOQUEADA));
        kpis.setTareasVencidas(contarTareasVencidas(tareas));
        kpis.setTareasSinResponsable(contarTareasSinResponsable(tareas));
        kpis.setAvancePromedio(redondear(calcularAvancePromedio(tareas)));
        kpis.setPorcentajeCompletadas(redondear(calcularPorcentajeCompletadas(kpis.getTareasCompletadas(), total)));

        return kpis;
    }

    @Override
    public Tarea crear(Tarea tarea) {
        prepararTarea(tarea);
        return tareaRepository.save(tarea);
    }

    @Override
    public Tarea actualizar(Long id, Tarea tareaActualizada) {
        Tarea existente = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada: " + id));

        existente.setProyectoId(tareaActualizada.getProyectoId());
        existente.setTitulo(tareaActualizada.getTitulo());
        existente.setDescripcion(tareaActualizada.getDescripcion());
        existente.setEstado(tareaActualizada.getEstado());
        existente.setAvance(tareaActualizada.getAvance());
        existente.setPrioridad(tareaActualizada.getPrioridad());
        existente.setFechaInicio(tareaActualizada.getFechaInicio());
        existente.setFechaFin(tareaActualizada.getFechaFin());
        existente.setResponsableIds(tareaActualizada.getResponsableIds());

        prepararTarea(existente);
        return tareaRepository.save(existente);
    }

    @Override
    public Tarea actualizarEstado(Long id, ActualizarEstadoTareaRequest request) {
        Tarea existente = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada: " + id));

        existente.setEstado(request.getEstado());
        existente.setAvance(request.getAvance());
        aplicarReglasDeAvance(existente);

        return tareaRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        tareaRepository.deleteById(id);
    }

    private void prepararTarea(Tarea tarea) {
        // Valores por defecto para evitar nulls en la base de datos.
        if (tarea.getEstado() == null) {
            tarea.setEstado(EstadoTarea.PENDIENTE);
        }
        if (tarea.getAvance() == null) {
            tarea.setAvance(0);
        }
        if (tarea.getPrioridad() == null) {
            tarea.setPrioridad(PrioridadTarea.MEDIA);
        }
        if (tarea.getResponsableIds() == null) {
            tarea.setResponsableIds(new ArrayList<>());
        }

        validarFechas(tarea);
        aplicarReglasDeAvance(tarea);
    }

    private void validarFechas(Tarea tarea) {
        // Regla de negocio: una tarea no puede terminar antes de iniciar.
        if (tarea.getFechaInicio() != null
                && tarea.getFechaFin() != null
                && tarea.getFechaFin().isBefore(tarea.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    private void aplicarReglasDeAvance(Tarea tarea) {
        // Regla principal: una tarea completada siempre representa 100% real.
        if (tarea.getEstado() == EstadoTarea.COMPLETADA) {
            tarea.setAvance(100);
            return;
        }

        // Una tarea bloqueada tiene un impedimento, por lo tanto no debe
        // contarse como terminada aunque antes haya estado al 100%.
        if (tarea.getEstado() == EstadoTarea.BLOQUEADA) {
            if (tarea.getAvance() != null && tarea.getAvance() >= 100) {
                tarea.setAvance(99);
            }
            return;
        }

        // Si llega a 100% y no esta bloqueada, el sistema la marca completada.
        if (tarea.getAvance() != null && tarea.getAvance() == 100) {
            tarea.setEstado(EstadoTarea.COMPLETADA);
            return;
        }

        // Si una tarea pendiente ya tiene avance, pasa automaticamente a progreso.
        if (tarea.getEstado() == EstadoTarea.PENDIENTE
                && tarea.getAvance() != null
                && tarea.getAvance() > 0) {
            tarea.setEstado(EstadoTarea.EN_PROGRESO);
        }
    }

    private int contarPorEstado(List<Tarea> tareas, EstadoTarea estado) {
        return (int) tareas.stream()
                .filter(tarea -> tarea.getEstado() == estado)
                .count();
    }

    private int contarTareasVencidas(List<Tarea> tareas) {
        LocalDate hoy = LocalDate.now();
        return (int) tareas.stream()
                .filter(tarea -> tarea.getFechaFin() != null)
                .filter(tarea -> tarea.getFechaFin().isBefore(hoy))
                .filter(tarea -> tarea.getEstado() != EstadoTarea.COMPLETADA)
                .count();
    }

    private int contarTareasSinResponsable(List<Tarea> tareas) {
        return (int) tareas.stream()
                .filter(tarea -> tarea.getResponsableIds() == null || tarea.getResponsableIds().isEmpty())
                .count();
    }

    private double calcularAvancePromedio(List<Tarea> tareas) {
        return tareas.stream()
                .mapToInt(tarea -> tarea.getAvance() != null ? tarea.getAvance() : 0)
                .average()
                .orElse(0);
    }

    private double calcularPorcentajeCompletadas(int completadas, int total) {
        if (total == 0) {
            return 0;
        }
        return (completadas * 100.0) / total;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
