package com.innovatech.msauth.dto;

// DTO que retorna el token JWT al frontend después de login exitoso
public class LoginResponse {
    private String token;
    private String rol;
    private String username;

    public LoginResponse(String token, String rol, String username) {
        this.token = token;
        this.rol = rol;
        this.username = username;
    }

    public String getToken() { return token; }
    public String getRol() { return rol; }
    public String getUsername() { return username; }
}