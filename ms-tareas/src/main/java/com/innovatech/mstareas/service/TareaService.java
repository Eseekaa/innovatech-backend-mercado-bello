package com.innovatech.mstareas.service;

import com.innovatech.mstareas.dto.ActualizarEstadoTareaRequest;
import com.innovatech.mstareas.model.Tarea;
import java.util.List;
import java.util.Optional;

// Contrato del servicio. Define que operaciones existen para tareas.
public interface TareaService {
    List<Tarea> obtenerTodas();
    Optional<Tarea> obtenerPorId(Long id);
    List<Tarea> obtenerPorProyecto(Long proyectoId);
    List<Tarea> obtenerPorResponsable(Long responsableId);
    Tarea crear(Tarea tarea);
    Tarea actualizar(Long id, Tarea tareaActualizada);
    Tarea actualizarEstado(Long id, ActualizarEstadoTareaRequest request);
    void eliminar(Long id);
}
