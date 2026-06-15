package com.innovatech.mstareas.service;

import com.innovatech.mstareas.dto.ActualizarEstadoTareaRequest;
import com.innovatech.mstareas.dto.TareaKpiDTO;
import com.innovatech.mstareas.dto.TareaKpiPorProyectoDTO;
import com.innovatech.mstareas.dto.TareaKpiPorResponsableDTO;
import com.innovatech.mstareas.model.EstadoTarea;
import com.innovatech.mstareas.model.PrioridadTarea;
import com.innovatech.mstareas.model.Tarea;
import com.innovatech.mstareas.repository.TareaRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
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
        return construirKpi(tareaRepository.findAll());
    }

    @Override
    public List<TareaKpiPorProyectoDTO> obtenerKpisPorProyecto() {
        // Agrupa todas las tareas por proyectoId para entregar un reporte
        // ejecutivo por proyecto sin que el frontend tenga que calcularlo.
        return tareaRepository.findAll().stream()
                .filter(tarea -> tarea.getProyectoId() != null)
                .collect(Collectors.groupingBy(Tarea::getProyectoId))
                .entrySet().stream()
                .map(entry -> convertirAReporteProyecto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(TareaKpiPorProyectoDTO::getProyectoId))
                .toList();
    }

    @Override
    public List<TareaKpiPorResponsableDTO> obtenerKpisPorResponsable() {
        // Una tarea puede tener varios responsables. Por eso armamos el mapa
        // manualmente: la misma tarea cuenta para cada responsable asignado.
        Map<Long, List<Tarea>> tareasPorResponsable = new TreeMap<>();
        for (Tarea tarea : tareaRepository.findAll()) {
            if (tarea.getResponsableIds() == null || tarea.getResponsableIds().isEmpty()) {
                continue;
            }
            for (Long responsableId : tarea.getResponsableIds()) {
                tareasPorResponsable
                        .computeIfAbsent(responsableId, id -> new ArrayList<>())
                        .add(tarea);
            }
        }

        return tareasPorResponsable.entrySet().stream()
                .map(entry -> convertirAReporteResponsable(entry.getKey(), entry.getValue()))
                .toList();
    }

    private TareaKpiDTO construirKpi(List<Tarea> tareas) {
        int total = tareas.size();

        TareaKpiDTO kpis = new TareaKpiDTO();
        kpis.setTotalTareas(total);
        kpis.setTareasPendientes(contarPorEstado(tareas, EstadoTarea.PENDIENTE));
        kpis.setTareasEnProgreso(contarPorEstado(tareas, EstadoTarea.EN_PROGRESO));
        kpis.setTareasCompletadas(contarPorEstado(tareas, EstadoTarea.COMPLETADA));
        kpis.setTareasAprobadas(contarTareasAprobadas(tareas));
        kpis.setTareasPendientesAprobacion(contarTareasPendientesAprobacion(tareas));
        kpis.setTareasBloqueadas(contarPorEstado(tareas, EstadoTarea.BLOQUEADA));
        kpis.setTareasVencidas(contarTareasVencidas(tareas));
        kpis.setTareasSinResponsable(contarTareasSinResponsable(tareas));
        kpis.setAvancePromedio(redondear(calcularAvancePromedio(tareas)));
        kpis.setPorcentajeCompletadas(redondear(calcularPorcentajeCompletadas(kpis.getTareasCompletadas(), total)));

        return kpis;
    }

    private TareaKpiPorProyectoDTO convertirAReporteProyecto(Long proyectoId, List<Tarea> tareas) {
        TareaKpiDTO base = construirKpi(tareas);
        TareaKpiPorProyectoDTO reporte = new TareaKpiPorProyectoDTO();
        reporte.setProyectoId(proyectoId);
        reporte.setTotalTareas(base.getTotalTareas());
        reporte.setTareasPendientes(base.getTareasPendientes());
        reporte.setTareasEnProgreso(base.getTareasEnProgreso());
        reporte.setTareasCompletadas(base.getTareasCompletadas());
        reporte.setTareasAprobadas(base.getTareasAprobadas());
        reporte.setTareasPendientesAprobacion(base.getTareasPendientesAprobacion());
        reporte.setTareasBloqueadas(base.getTareasBloqueadas());
        reporte.setTareasVencidas(base.getTareasVencidas());
        reporte.setTareasSinResponsable(base.getTareasSinResponsable());
        reporte.setAvancePromedio(base.getAvancePromedio());
        reporte.setPorcentajeCompletadas(base.getPorcentajeCompletadas());
        return reporte;
    }

    private TareaKpiPorResponsableDTO convertirAReporteResponsable(Long responsableId, List<Tarea> tareas) {
        TareaKpiDTO base = construirKpi(tareas);
        TareaKpiPorResponsableDTO reporte = new TareaKpiPorResponsableDTO();
        reporte.setResponsableId(responsableId);
        reporte.setTotalTareas(base.getTotalTareas());
        reporte.setTareasPendientes(base.getTareasPendientes());
        reporte.setTareasEnProgreso(base.getTareasEnProgreso());
        reporte.setTareasCompletadas(base.getTareasCompletadas());
        reporte.setTareasAprobadas(base.getTareasAprobadas());
        reporte.setTareasPendientesAprobacion(base.getTareasPendientesAprobacion());
        reporte.setTareasBloqueadas(base.getTareasBloqueadas());
        reporte.setTareasVencidas(base.getTareasVencidas());
        reporte.setAvancePromedio(base.getAvancePromedio());
        reporte.setPorcentajeCompletadas(base.getPorcentajeCompletadas());
        return reporte;
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
        if (tareaActualizada.getVistoBueno() != null) {
            existente.setVistoBueno(tareaActualizada.getVistoBueno());
        }
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
    public Tarea cambiarVistoBueno(Long id, boolean vistoBueno) {
        Tarea existente = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada: " + id));

        // Regla de negocio: el visto bueno representa aceptacion final del
        // trabajo, por eso solo puede activarse si la tarea ya esta completada.
        if (vistoBueno && existente.getEstado() != EstadoTarea.COMPLETADA) {
            throw new IllegalArgumentException("Solo se puede dar visto bueno a tareas completadas");
        }

        existente.setVistoBueno(vistoBueno);
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
        if (tarea.getVistoBueno() == null) {
            tarea.setVistoBueno(false);
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

        // Si la tarea deja de estar completada, el visto bueno deja de ser valido.
        tarea.setVistoBueno(false);

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

    private int contarTareasAprobadas(List<Tarea> tareas) {
        return (int) tareas.stream()
                .filter(tarea -> tarea.getEstado() == EstadoTarea.COMPLETADA)
                .filter(tarea -> Boolean.TRUE.equals(tarea.getVistoBueno()))
                .count();
    }

    private int contarTareasPendientesAprobacion(List<Tarea> tareas) {
        return (int) tareas.stream()
                .filter(tarea -> tarea.getEstado() == EstadoTarea.COMPLETADA)
                .filter(tarea -> !Boolean.TRUE.equals(tarea.getVistoBueno()))
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
