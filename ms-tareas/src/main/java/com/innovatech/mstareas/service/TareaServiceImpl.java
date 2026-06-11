package com.innovatech.mstareas.service;

import com.innovatech.mstareas.dto.ActualizarEstadoTareaRequest;
import com.innovatech.mstareas.model.EstadoTarea;
import com.innovatech.mstareas.model.PrioridadTarea;
import com.innovatech.mstareas.model.Tarea;
import com.innovatech.mstareas.repository.TareaRepository;
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
        // Si llega a 100%, la tarea queda completada.
        if (tarea.getAvance() != null && tarea.getAvance() == 100) {
            tarea.setEstado(EstadoTarea.COMPLETADA);
        }

        // Si esta completada manualmente, su avance debe ser 100%.
        if (tarea.getEstado() == EstadoTarea.COMPLETADA) {
            tarea.setAvance(100);
        }
    }
}
