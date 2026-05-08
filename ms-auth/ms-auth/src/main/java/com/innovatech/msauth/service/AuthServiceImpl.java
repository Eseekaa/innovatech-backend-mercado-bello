package com.innovatech.msauth.service;

import com.innovatech.msauth.dto.LoginRequest;
import com.innovatech.msauth.dto.LoginResponse;
import com.innovatech.msauth.dto.RegisterRequest;
import com.innovatech.msauth.model.Usuario;
import com.innovatech.msauth.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Implementación del servicio de autenticación
// Contiene la lógica real de registro y login
@Service
public class AuthServiceImpl implements AuthService {

    // Dependency Injection: Spring inyecta estos objetos automáticamente
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UsuarioRepository usuarioRepository,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // Registra un nuevo usuario encriptando su contraseña
    @Override
    public Usuario register(RegisterRequest request) {
        // Verifica que el username no esté en uso
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username ya existe");
        }
        // Verifica que el email no esté en uso
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email ya existe");
        }
        // Valida que la contraseña cumpla los requisitos mínimos:
        // mínimo 8 caracteres, al menos una letra, un número y un @
        if (!request.isPasswordValida()) {
            throw new RuntimeException("La contraseña debe tener mínimo 8 caracteres, letras, números y @");
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        // BCrypt encripta la contraseña - nunca se guarda en texto plano
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEmail(request.getEmail());
        // Si no se especifica rol, se asigna USUARIO por defecto
        usuario.setRol(request.getRol() != null ? request.getRol() : "USUARIO");
        return usuarioRepository.save(usuario);
    }

    // Autentica un usuario y genera un token JWT
    @Override
    public LoginResponse login(LoginRequest request) {
        // Busca el usuario en la base de datos por username
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // Verifica que la contraseña ingresada coincida con la encriptada
        // BCrypt compara automáticamente el hash
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        // Genera el token JWT con username y rol
        String token = jwtService.generateToken(usuario.getUsername(), usuario.getRol());
        return new LoginResponse(token, usuario.getRol(), usuario.getUsername());
    }
}