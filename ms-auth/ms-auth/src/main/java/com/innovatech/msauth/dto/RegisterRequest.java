package com.innovatech.msauth.dto;

// DTO para registrar un nuevo usuario
// Incluye validación de contraseña con mínimo 8 caracteres, letras, números y @
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    // Rol por defecto USUARIO, puede ser JEFE_PROYECTO o ADMIN
    private String rol;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    // Valida que la contraseña cumpla los requisitos:
    // - Mínimo 8 caracteres
    // - Al menos una letra
    // - Al menos un número
    // - Al menos un @
    public boolean isPasswordValida() {
        if (password == null || password.length() < 8) return false;
        boolean tienLetra = password.chars().anyMatch(Character::isLetter);
        boolean tieneNumero = password.chars().anyMatch(Character::isDigit);
        boolean tieneArroba = password.contains("@");
        return tienLetra && tieneNumero && tieneArroba;
    }
}