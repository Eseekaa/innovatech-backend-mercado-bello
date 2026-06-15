package com.innovatech.mstareas.dto;

// DTO de reporte: resume el avance de tareas agrupado por responsable.
// Ayuda a ver carga de trabajo, tareas vencidas y avance por empleado.
public class TareaKpiPorResponsableDTO {

    private Long responsableId;
    private int totalTareas;
    private int tareasPendientes;
    private int tareasEnProgreso;
    private int tareasCompletadas;
    private int tareasAprobadas;
    private int tareasPendientesAprobacion;
    private int tareasBloqueadas;
    private int tareasVencidas;
    private double avancePromedio;
    private double porcentajeCompletadas;

    public Long getResponsableId() {
        return responsableId;
    }

    public void setResponsableId(Long responsableId) {
        this.responsableId = responsableId;
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

    public int getTareasAprobadas() {
        return tareasAprobadas;
    }

    public void setTareasAprobadas(int tareasAprobadas) {
        this.tareasAprobadas = tareasAprobadas;
    }

    public int getTareasPendientesAprobacion() {
        return tareasPendientesAprobacion;
    }

    public void setTareasPendientesAprobacion(int tareasPendientesAprobacion) {
        this.tareasPendientesAprobacion = tareasPendientesAprobacion;
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
