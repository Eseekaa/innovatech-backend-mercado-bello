package com.innovatech.bff.dto;

import java.util.List;

// DTO para transferir datos de empleados entre BFF y frontend.
public class RecursoDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String cargo;
    private String departamento;
    private String disponibilidad;
    private String nivelExperiencia;
    // Compatibilidad: primer proyecto asignado, visible como ID_PROYECTO en H2.
    private Long idProyecto;
    // Relacion completa: permite asignar un empleado a varios proyectos.
    private List<Long> idProyectos;

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
    public void setIdProyecto(Long idProyecto) { this.idProyecto = idProyecto; }
    public List<Long> getIdProyectos() { return idProyectos; }
    public void setIdProyectos(List<Long> idProyectos) { this.idProyectos = idProyectos; }
}
