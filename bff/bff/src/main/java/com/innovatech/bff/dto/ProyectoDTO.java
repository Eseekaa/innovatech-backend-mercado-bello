package com.innovatech.bff.dto;

import java.time.LocalDate;

// DTO = Data Transfer Object
// Transfiere datos de proyectos entre BFF y frontend sin exponer la entidad
public class ProyectoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String responsable;
    // Estado de aprobacion del usuario normal sobre el proyecto terminado.
    private Boolean vistoBueno;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public Boolean getVistoBueno() { return vistoBueno; }
    public void setVistoBueno(Boolean vistoBueno) { this.vistoBueno = vistoBueno; }
}
