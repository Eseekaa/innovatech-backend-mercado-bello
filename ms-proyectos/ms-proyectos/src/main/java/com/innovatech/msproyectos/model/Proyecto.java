package com.innovatech.msproyectos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// @Entity indica que esta clase es una tabla en la base de datos
@Entity
@Table(name = "proyectos")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    // Estado: "ACTIVO", "EN_PAUSA", "COMPLETADO", "CANCELADO"
    @Column(nullable = false)
    private String estado;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "responsable")
    private String responsable;

    // Indica si el usuario final ya dio el visto bueno al proyecto terminado.
    @Column(name = "visto_bueno", nullable = false)
    private Boolean vistoBueno = false;

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
