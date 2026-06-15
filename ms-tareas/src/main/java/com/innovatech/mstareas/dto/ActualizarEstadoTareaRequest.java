package com.innovatech.mstareas.dto;

import com.innovatech.mstareas.model.EstadoTarea;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// DTO pequeno para actualizar solo estado y avance sin tocar toda la tarea.
public class ActualizarEstadoTareaRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoTarea estado;

    @NotNull(message = "El avance es obligatorio")
    @Min(value = 0, message = "El avance minimo es 0")
    @Max(value = 100, message = "El avance maximo es 100")
    private Integer avance;

    public EstadoTarea getEstado() { return estado; }
    public void setEstado(EstadoTarea estado) { this.estado = estado; }
    public Integer getAvance() { return avance; }
    public void setAvance(Integer avance) { this.avance = avance; }
}
