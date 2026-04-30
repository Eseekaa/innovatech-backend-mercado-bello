package com.innovatech.msauth.controller;

import com.innovatech.msauth.dto.LoginRequest;
import com.innovatech.msauth.dto.LoginResponse;
import com.innovatech.msauth.dto.RegisterRequest;
import com.innovatech.msauth.model.Usuario;
import com.innovatech.msauth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controller que expone los endpoints de autenticación
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/register
    // Registra un nuevo usuario en el sistema
    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // POST /api/auth/login
    // Autentica usuario y retorna token JWT
    // El frontend guarda este token y lo envía en cada petición
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}