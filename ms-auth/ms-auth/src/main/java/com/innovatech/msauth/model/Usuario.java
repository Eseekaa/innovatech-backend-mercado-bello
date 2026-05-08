package com.innovatech.msauth.model;

import jakarta.persistence.*;

// @Entity indica que esta clase representa una tabla en la base de datos
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    // Contraseña encriptada con BCrypt - nunca se guarda en texto plano
    @Column(nullable = false)
    private String password;

    // 3 roles disponibles:
    // ADMIN - administra el proyecto completo
    // JEFE_PROYECTO - puede modificar recursos
    // USUARIO - usuario normal que puede dar visto bueno a proyectos
    @Column(nullable = false)
    private String rol;

    @Column(nullable = false, unique = true)
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}