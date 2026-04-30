package com.innovatech.msauth.repository;

import com.innovatech.msauth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Repository Pattern: maneja el acceso a datos de usuarios
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Busca usuario por username para el proceso de login
    Optional<Usuario> findByUsername(String username);
    // Verifica si ya existe un usuario con ese username
    boolean existsByUsername(String username);
    // Verifica si ya existe un usuario con ese email
    boolean existsByEmail(String email);
}