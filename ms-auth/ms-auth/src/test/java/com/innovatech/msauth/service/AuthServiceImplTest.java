package com.innovatech.msauth.service;

import com.innovatech.msauth.dto.LoginRequest;
import com.innovatech.msauth.dto.LoginResponse;
import com.innovatech.msauth.dto.RegisterRequest;
import com.innovatech.msauth.model.Usuario;
import com.innovatech.msauth.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Pruebas unitarias: se simulan repositorio, JWT y BCrypt con Mockito.
// Esto permite probar solo las reglas de AuthServiceImpl sin iniciar Spring ni H2.
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(usuarioRepository, jwtService, passwordEncoder);
    }

    @Test
    void registerDebeGuardarPasswordEncriptadaYRolUsuarioPorDefecto() {
        RegisterRequest request = crearRegistro(null, "Clave123@");
        when(usuarioRepository.existsByUsername("matias")).thenReturn(false);
        when(usuarioRepository.existsByEmail("matias@innovatech.cl")).thenReturn(false);
        when(passwordEncoder.encode("Clave123@")).thenReturn("hash-bcrypt");
        when(usuarioRepository.save(org.mockito.ArgumentMatchers.any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = authService.register(request);

        assertEquals("matias", resultado.getUsername());
        assertEquals("matias@innovatech.cl", resultado.getEmail());
        assertEquals("hash-bcrypt", resultado.getPassword());
        assertEquals("USUARIO", resultado.getRol());
        verify(passwordEncoder).encode("Clave123@");
        verify(usuarioRepository).save(resultado);
    }

    @Test
    void registerDebeConservarRolSolicitado() {
        RegisterRequest request = crearRegistro("JEFE_PROYECTO", "Clave123@");
        when(usuarioRepository.existsByUsername("matias")).thenReturn(false);
        when(usuarioRepository.existsByEmail("matias@innovatech.cl")).thenReturn(false);
        when(passwordEncoder.encode("Clave123@")).thenReturn("hash-bcrypt");
        when(usuarioRepository.save(org.mockito.ArgumentMatchers.any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = authService.register(request);

        assertEquals("JEFE_PROYECTO", resultado.getRol());
    }

    @Test
    void registerDebeRechazarUsernameDuplicado() {
        RegisterRequest request = crearRegistro(null, "Clave123@");
        when(usuarioRepository.existsByUsername("matias")).thenReturn(true);

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> authService.register(request));

        assertEquals("Username ya existe", error.getMessage());
        verify(usuarioRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void registerDebeRechazarEmailDuplicado() {
        RegisterRequest request = crearRegistro(null, "Clave123@");
        when(usuarioRepository.existsByUsername("matias")).thenReturn(false);
        when(usuarioRepository.existsByEmail("matias@innovatech.cl")).thenReturn(true);

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> authService.register(request));

        assertEquals("Email ya existe", error.getMessage());
        verify(usuarioRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void registerDebeRechazarPasswordQueNoCumpleLasReglas() {
        RegisterRequest request = crearRegistro(null, "corta");
        when(usuarioRepository.existsByUsername("matias")).thenReturn(false);
        when(usuarioRepository.existsByEmail("matias@innovatech.cl")).thenReturn(false);

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> authService.register(request));

        assertTrue(error.getMessage().contains("8 caracteres"));
        verify(passwordEncoder, never()).encode(org.mockito.ArgumentMatchers.anyString());
        verify(usuarioRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void loginDebeRetornarTokenRolYUsernameCuandoCredencialesSonCorrectas() {
        LoginRequest request = crearLogin("matias", "Clave123@");
        Usuario usuario = crearUsuario("matias", "hash-bcrypt", "ADMIN");
        when(usuarioRepository.findByUsername("matias")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Clave123@", "hash-bcrypt")).thenReturn(true);
        when(jwtService.generateToken("matias", "ADMIN")).thenReturn("jwt-generado");

        LoginResponse respuesta = authService.login(request);

        assertEquals("jwt-generado", respuesta.getToken());
        assertEquals("ADMIN", respuesta.getRol());
        assertEquals("matias", respuesta.getUsername());
    }

    @Test
    void loginDebeRechazarUsuarioInexistente() {
        LoginRequest request = crearLogin("desconocido", "Clave123@");
        when(usuarioRepository.findByUsername("desconocido")).thenReturn(Optional.empty());

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("Usuario no encontrado", error.getMessage());
        verify(passwordEncoder, never()).matches(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    void loginDebeRechazarPasswordIncorrecta() {
        LoginRequest request = crearLogin("matias", "Incorrecta1@");
        Usuario usuario = crearUsuario("matias", "hash-bcrypt", "USUARIO");
        when(usuarioRepository.findByUsername("matias")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Incorrecta1@", "hash-bcrypt")).thenReturn(false);

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertTrue(error.getMessage().endsWith("incorrecta"));
        verify(jwtService, never()).generateToken(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString());
    }

    private RegisterRequest crearRegistro(String rol, String password) {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("matias");
        request.setEmail("matias@innovatech.cl");
        request.setPassword(password);
        request.setRol(rol);
        return request;
    }

    private LoginRequest crearLogin(String username, String password) {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);
        return request;
    }

    private Usuario crearUsuario(String username, String password, String rol) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setRol(rol);
        usuario.setEmail(username + "@innovatech.cl");
        return usuario;
    }
}
