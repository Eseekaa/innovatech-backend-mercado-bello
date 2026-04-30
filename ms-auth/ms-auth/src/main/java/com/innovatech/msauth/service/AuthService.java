package com.innovatech.msauth.service;

import com.innovatech.msauth.dto.LoginRequest;
import com.innovatech.msauth.dto.LoginResponse;
import com.innovatech.msauth.dto.RegisterRequest;
import com.innovatech.msauth.model.Usuario;

// Interfaz que define el contrato del servicio de autenticación
// Patrón Strategy: define QUÉ hace el servicio sin implementarlo
public interface AuthService {
    // Registra un nuevo usuario en el sistema
    Usuario register(RegisterRequest request);
    // Autentica un usuario y retorna un token JWT
    LoginResponse login(LoginRequest request);
}