package com.innovatech.mstareas.dto;

// DTO de reporte: resume el avance de tareas agrupado por proyecto.
// Sirve para responder preguntas como "que proyecto va mas atrasado".
public class TareaKpiPorProyectoDTO {

    private Long proyectoId;
    private int totalTareas;
    private int tareasPendientes;
    private int tareasEnProgreso;
    private int tareasCompletadas;
    private int tareasBloqueadas;
    private int tareasVencidas;
    private int tareasSinResponsable;
    private double avancePromedio;
    private double porcentajeCompletadas;

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public int getTotalTareas() {
        return totalTareas;
    }

    public void setTotalTareas(int totalTareas) {
        this.totalTareas = totalTareas;
    }

    public int getTareasPendientes() {
        return tareasPendientes;
    }

    public void setTareasPendientes(int tareasPendientes) {
        this.tareasPendientes = tareasPendientes;
    }

    public int getTareasEnProgreso() {
        return tareasEnProgreso;
    }

    public void setTareasEnProgreso(int tareasEnProgreso) {
        this.tareasEnProgreso = tareasEnProgreso;
    }

    public int getTareasCompletadas() {
        return tareasCompletadas;
    }

    public void setTareasCompletadas(int tareasCompletadas) {
        this.tareasCompletadas = tareasCompletadas;
    }

    public int getTareasBloqueadas() {
        return tareasBloqueadas;
    }

    public void setTareasBloqueadas(int tareasBloqueadas) {
        this.tareasBloqueadas = tareasBloqueadas;
    }

    public int getTareasVencidas() {
        return tareasVencidas;
    }

    public void setTareasVencidas(int tareasVencidas) {
        this.tareasVencidas = tareasVencidas;
    }

    public int getTareasSinResponsable() {
        return tareasSinResponsable;
    }

    public void setTareasSinResponsable(int tareasSinResponsable) {
        this.tareasSinResponsable = tareasSinResponsable;
    }

    public double getAvancePromedio() {
        return avancePromedio;
    }

    public void setAvancePromedio(double avancePromedio) {
        this.avancePromedio = avancePromedio;
    }

    public double getPorcentajeCompletadas() {
        return porcentajeCompletadas;
    }

    public void setPorcentajeCompletadas(double porcentajeCompletadas) {
        this.porcentajeCompletadas = porcentajeCompletadas;
    }
}
