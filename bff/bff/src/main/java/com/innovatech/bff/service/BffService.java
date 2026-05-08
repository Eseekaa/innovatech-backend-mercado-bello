package com.innovatech.bff.service;

import com.innovatech.bff.dto.DashboardDTO;
import com.innovatech.bff.dto.ProyectoDTO;
import com.innovatech.bff.dto.RecursoDTO;
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
}
