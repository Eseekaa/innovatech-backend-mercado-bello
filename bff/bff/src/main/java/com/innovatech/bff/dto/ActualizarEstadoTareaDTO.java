package com.innovatech.bff.dto;

// DTO especifico para cambiar solo estado y avance de una tarea.
// Es util para que un usuario actualice progreso sin editar toda la tarea.
public class ActualizarEstadoTareaDTO {
    private String estado;
    private Integer avance;

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getAvance() { return avance; }
    public void setAvance(Integer avance) { this.avance = avance; }
}
