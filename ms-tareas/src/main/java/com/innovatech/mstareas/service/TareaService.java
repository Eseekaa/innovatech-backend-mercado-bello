package com.innovatech.mstareas.service;

import com.innovatech.mstareas.dto.ActualizarEstadoTareaRequest;
import com.innovatech.mstareas.dto.TareaKpiDTO;
import com.innovatech.mstareas.dto.TareaKpiPorProyectoDTO;
import com.innovatech.mstareas.dto.TareaKpiPorResponsableDTO;
import com.innovatech.mstareas.model.Tarea;
import java.util.List;
import java.util.Optional;

// Contrato del servicio. Define que operaciones existen para tareas.
public interface TareaService {
    List<Tarea> obtenerTodas();
    Optional<Tarea> obtenerPorId(Long id);
    List<Tarea> obtenerPorProyecto(Long proyectoId);
    List<Tarea> obtenerPorResponsable(Long responsableId);
    TareaKpiDTO obtenerKpis();
    List<TareaKpiPorProyectoDTO> obtenerKpisPorProyecto();
    List<TareaKpiPorResponsableDTO> obtenerKpisPorResponsable();
    Tarea crear(Tarea tarea);
    Tarea actualizar(Long id, Tarea tareaActualizada);
    Tarea actualizarEstado(Long id, ActualizarEstadoTareaRequest request);
    Tarea cambiarVistoBueno(Long id, boolean vistoBueno);
    void eliminar(Long id);
}
