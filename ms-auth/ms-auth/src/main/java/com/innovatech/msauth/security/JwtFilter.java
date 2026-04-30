package com.innovatech.msauth.security;

import com.innovatech.msauth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

// Filtro que intercepta cada petición HTTP para verificar el token JWT
// Se ejecuta antes de que llegue al controller
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Obtiene el header Authorization de la petición
        // Formato esperado: "Bearer eyJhbGc..."
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extrae el token removiendo "Bearer "
            String token = authHeader.substring(7);

            if (jwtService.isTokenValid(token)) {
                // Extrae username y rol del token
                String username = jwtService.extractUsername(token);
                String rol = jwtService.extractRol(token);

                // Registra al usuario como autenticado en Spring Security
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                username, null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                        );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        // Continúa con la siguiente etapa del filtro
        filterChain.doFilter(request, response);
    }
}