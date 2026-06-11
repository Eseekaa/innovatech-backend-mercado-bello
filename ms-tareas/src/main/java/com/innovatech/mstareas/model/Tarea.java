package com.innovatech.mstareas.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// @Entity convierte esta clase en una tabla H2 llamada tareas.
@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // No se usa foreign key real porque el proyecto vive en otro microservicio.
    @NotNull(message = "La tarea debe pertenecer a un proyecto")
    @Column(name = "proyecto_id", nullable = false)
    private Long proyectoId;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(min = 3, max = 120, message = "El titulo debe tener entre 3 y 120 caracteres")
    @Column(nullable = false)
    private String titulo;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(min = 5, max = 500, message = "La descripcion debe tener entre 5 y 500 caracteres")
    @Column(nullable = false, length = 500)
    private String descripcion;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTarea estado = EstadoTarea.PENDIENTE;

    // Avance en porcentaje. Sirve para KPIs y dashboard.
    @NotNull(message = "El avance es obligatorio")
    @Min(value = 0, message = "El avance minimo es 0")
    @Max(value = 100, message = "El avance maximo es 100")
    @Column(nullable = false)
    private Integer avance = 0;

    @NotNull(message = "La prioridad es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadTarea prioridad = PrioridadTarea.MEDIA;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    // Lista de IDs de recursos responsables. Los datos completos viven en ms-recursos.
    @ElementCollection
    @CollectionTable(name = "tarea_responsables", joinColumns = @JoinColumn(name = "tarea_id"))
    @Column(name = "responsable_id")
    private List<Long> responsableIds = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProyectoId() { return proyectoId; }
    public void setProyectoId(Long proyectoId) { this.proyectoId = proyectoId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public EstadoTarea getEstado() { return estado; }
    public void setEstado(EstadoTarea estado) { this.estado = estado; }
    public Integer getAvance() { return avance; }
    public void setAvance(Integer avance) { this.avance = avance; }
    public PrioridadTarea getPrioridad() { return prioridad; }
    public void setPrioridad(PrioridadTarea prioridad) { this.prioridad = prioridad; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public List<Long> getResponsableIds() { return responsableIds; }
    public void setResponsableIds(List<Long> responsableIds) {
        this.responsableIds = responsableIds != null ? responsableIds : new ArrayList<>();
    }
}
