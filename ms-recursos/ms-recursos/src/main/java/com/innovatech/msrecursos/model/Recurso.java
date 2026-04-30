package com.innovatech.msrecursos.model;

import jakarta.persistence.*;

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
}