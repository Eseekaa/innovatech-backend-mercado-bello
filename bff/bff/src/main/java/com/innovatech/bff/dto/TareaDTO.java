package com.innovatech.bff.dto;

import java.time.LocalDate;
import java.util.List;

// DTO para mover tareas entre frontend, BFF y ms-tareas.
// El BFF no guarda datos: solo transporta y combina respuestas.
public class TareaDTO {
    private Long id;
    private Long proyectoId;
    private String titulo;
    private String descripcion;
    private String estado;
    private Integer avance;
    private String prioridad;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean vistoBueno;
    private List<Long> responsableIds;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProyectoId() { return proyectoId; }
    public void setProyectoId(Long proyectoId) { this.proyectoId = proyectoId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getAvance() { return avance; }
    public void setAvance(Integer avance) { this.avance = avance; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public Boolean getVistoBueno() { return vistoBueno; }
    public void setVistoBueno(Boolean vistoBueno) { this.vistoBueno = vistoBueno; }
    public List<Long> getResponsableIds() { return responsableIds; }
    public void setResponsableIds(List<Long> responsableIds) { this.responsableIds = responsableIds; }
}
