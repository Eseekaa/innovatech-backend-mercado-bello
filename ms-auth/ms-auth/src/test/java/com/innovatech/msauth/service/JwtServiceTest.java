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
        char reemplazo = token.charAt(token.length() - 1) == 'a' ? 'b' : 'a';
        String tokenManipulado = token.substring(0, token.length() - 1) + reemplazo;

        assertFalse(jwtService.isTokenValid(tokenManipulado));
    }
}
