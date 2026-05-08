package com.innovatech.msrecursos.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// @Entity indica que esta clase es una tabla en la base de datos
@Entity
@Table(name = "recursos")
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    // Email único para cada empleado
    @Column(nullable = false, unique = true)
    private String email;

    // Cargo: "Desarrollador Backend", "Diseñador UX", etc.
    @Column(nullable = false)
    private String cargo;

    // Departamento: "Desarrollo", "QA", "DevOps", etc.
    @Column(nullable = false)
    private String departamento;

    // Disponibilidad: "DISPONIBLE", "OCUPADO", "VACACIONES"
    @Column(nullable = false)
    private String disponibilidad;

    // Nivel: "JUNIOR", "SEMI_SENIOR", "SENIOR"
    @Column(nullable = false)
    private String nivelExperiencia;

    // ID del proyecto al que está asignado este recurso
    // Un recurso depende de un proyecto
    // Puede ser null si el recurso no está asignado a ningún proyecto
    @Column(name = "id_proyecto")
    private Long idProyecto;

    // Relacion real para permitir que un empleado participe en varios proyectos.
    // H2 crea una tabla RECURSO_PROYECTOS con RECURSO_ID e ID_PROYECTO.
    @ElementCollection
    @CollectionTable(name = "recurso_proyectos", joinColumns = @JoinColumn(name = "recurso_id"))
    @Column(name = "id_proyecto")
    private List<Long> idProyectos = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public String getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(String disponibilidad) { this.disponibilidad = disponibilidad; }
    public String getNivelExperiencia() { return nivelExperiencia; }
    public void setNivelExperiencia(String nivelExperiencia) { this.nivelExperiencia = nivelExperiencia; }
    public Long getIdProyecto() { return idProyecto; }
    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
        if (idProyecto != null && (idProyectos == null || idProyectos.isEmpty())) {
            this.idProyectos = new ArrayList<>();
            this.idProyectos.add(idProyecto);
        }
    }
    public List<Long> getIdProyectos() { return idProyectos; }
    public void setIdProyectos(List<Long> idProyectos) {
        this.idProyectos = idProyectos != null ? idProyectos : new ArrayList<>();
        this.idProyecto = this.idProyectos.isEmpty() ? null : this.idProyectos.get(0);
    }
}
