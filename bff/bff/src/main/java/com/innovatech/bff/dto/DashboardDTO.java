package com.innovatech.bff.dto;

import java.util.List;

// DTO del Dashboard: combina datos de proyectos y recursos en una sola respuesta
// Esta es la ventaja principal del BFF - agrega datos de múltiples microservicios
public class DashboardDTO {
    private List<ProyectoDTO> proyectos;
    private List<RecursoDTO> recursos;
    private List<TareaDTO> tareas;
    private TareaKpiDTO tareaKpis;
    private int totalProyectos;
    private int totalRecursos;
    private int recursosDisponibles;
    private int proyectosActivos;

    public List<ProyectoDTO> getProyectos() { return proyectos; }
    public void setProyectos(List<ProyectoDTO> proyectos) { this.proyectos = proyectos; }
    public List<RecursoDTO> getRecursos() { return recursos; }
    public void setRecursos(List<RecursoDTO> recursos) { this.recursos = recursos; }
    public List<TareaDTO> getTareas() { return tareas; }
    public void setTareas(List<TareaDTO> tareas) { this.tareas = tareas; }
    public TareaKpiDTO getTareaKpis() { return tareaKpis; }
    public void setTareaKpis(TareaKpiDTO tareaKpis) { this.tareaKpis = tareaKpis; }
    public int getTotalProyectos() { return totalProyectos; }
    public void setTotalProyectos(int totalProyectos) { this.totalProyectos = totalProyectos; }
    public int getTotalRecursos() { return totalRecursos; }
    public void setTotalRecursos(int totalRecursos) { this.totalRecursos = totalRecursos; }
    public int getRecursosDisponibles() { return recursosDisponibles; }
    public void setRecursosDisponibles(int recursosDisponibles) { this.recursosDisponibles = recursosDisponibles; }
    public int getProyectosActivos() { return proyectosActivos; }
    public void setProyectosActivos(int proyectosActivos) { this.proyectosActivos = proyectosActivos; }
}
