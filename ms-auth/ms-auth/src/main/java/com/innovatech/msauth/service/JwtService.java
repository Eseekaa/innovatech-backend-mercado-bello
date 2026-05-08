package com.innovatech.msauth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

// Servicio que maneja toda la lógica de JWT (JSON Web Token)
// JWT es el estándar para autenticación en APIs REST
@Service
public class JwtService {

    // Clave secreta para firmar los tokens - debe ser larga y segura
    private static final String SECRET = "innovatech-secret-key-2024-fullstack3-duocuc-segura";
    // Tiempo de expiración: 10 minutos en milisegundos (para presentación)
    private static final long EXPIRATION = 600000;

    // Genera la clave de firma a partir del SECRET
    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Genera un token JWT con el username y rol del usuario
    // Este token se envía al frontend después del login
    public String generateToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol) // Agrega el rol como dato extra en el token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrae el username del token JWT
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Extrae el rol del token JWT
    public String extractRol(String token) {
        return getClaims(token).get("rol", String.class);
    }

    // Verifica si el token es válido y no ha expirado
    public boolean isTokenValid(String token) {
        try {
            return getClaims(token).getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Decodifica el token y extrae toda la información
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}