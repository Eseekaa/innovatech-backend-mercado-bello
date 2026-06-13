package com.innovatech.bff.service;

import com.innovatech.bff.dto.DashboardDTO;
import com.innovatech.bff.dto.ProyectoDTO;
import com.innovatech.bff.dto.RecursoDTO;
import com.innovatech.bff.dto.TareaDTO;
import com.innovatech.bff.dto.TareaKpiDTO;
import com.innovatech.bff.dto.TareaKpiPorProyectoDTO;
import com.innovatech.bff.dto.TareaKpiPorResponsableDTO;
import com.innovatech.bff.dto.ActualizarEstadoTareaDTO;
import java.util.List;

// Interfaz que define el contrato del BFF
// Patrón Strategy: define QUÉ hace el BFF sin implementarlo
public interface BffService {
    DashboardDTO obtenerDashboard();
    List<ProyectoDTO> obtenerProyectos();
    ProyectoDTO crearProyecto(ProyectoDTO proyectoDTO);
    ProyectoDTO actualizarProyecto(Long id, ProyectoDTO proyectoDTO);
    void eliminarProyecto(Long id);
    List<RecursoDTO> obtenerRecursos();
    RecursoDTO crearRecurso(RecursoDTO recursoDTO);
    RecursoDTO actualizarRecurso(Long id, RecursoDTO recursoDTO);
    void eliminarRecurso(Long id);
    // Obtiene recursos filtrados por proyecto
    List<RecursoDTO> obtenerRecursosPorProyecto(Long idProyecto);
    // Asigna un empleado a un proyecto desde el BFF
    RecursoDTO asignarProyectoARecurso(Long id, Long idProyecto);
    // Operaciones de tareas expuestas por el BFF hacia el frontend
    List<TareaDTO> obtenerTareas();
    TareaKpiDTO obtenerKpisTareas();
    List<TareaKpiPorProyectoDTO> obtenerKpisTareasPorProyecto();
    List<TareaKpiPorResponsableDTO> obtenerKpisTareasPorResponsable();
    TareaDTO obtenerTareaPorId(Long id);
    List<TareaDTO> obtenerTareasPorProyecto(Long proyectoId);
    List<TareaDTO> obtenerTareasPorResponsable(Long responsableId);
    TareaDTO crearTarea(TareaDTO tareaDTO);
    TareaDTO actualizarTarea(Long id, TareaDTO tareaDTO);
    TareaDTO actualizarEstadoTarea(Long id, ActualizarEstadoTareaDTO dto);
    TareaDTO cambiarVistoBuenoTarea(Long id, boolean vistoBueno);
    void eliminarTarea(Long id);
}
