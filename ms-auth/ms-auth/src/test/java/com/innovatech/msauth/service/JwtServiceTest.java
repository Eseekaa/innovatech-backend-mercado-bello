package com.innovatech.msauth.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// JwtService no necesita mocks: el test genera tokens reales firmados y luego
// comprueba que puedan leerse y que una alteracion invalide su firma.
class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateTokenDebeGuardarUsernameYRol() {
        String token = jwtService.generateToken("admin1", "ADMIN");

        assertEquals("admin1", jwtService.extractUsername(token));
        assertEquals("ADMIN", jwtService.extractRol(token));
    }

    @Test
    void isTokenValidDebeAceptarTokenRecienGenerado() {
        String token = jwtService.generateToken("jefe1", "JEFE_PROYECTO");

        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValidDebeRechazarTextoQueNoEsJwt() {
        assertFalse(jwtService.isTokenValid("token-invalido"));
    }

    @Test
    void isTokenValidDebeRechazarTokenManipulado() {
        String token = jwtService.generateToken("usuario1", "USUARIO");
        String[] partes = token.split("\\.");
        char reemplazo = partes[2].charAt(0) == 'a' ? 'b' : 'a';
        partes[2] = reemplazo + partes[2].substring(1);
        String tokenManipulado = String.join(".", partes);

        assertFalse(jwtService.isTokenValid(tokenManipulado));
    }
}
