package com.innovatech.msproyectos.repository;

import com.innovatech.msproyectos.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Repository Pattern: maneja el acceso a datos de proyectos
// JpaRepository nos da métodos CRUD listos sin escribir SQL
@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    // Spring genera el SQL automáticamente por el nombre del método
    List<Proyecto> findByEstado(String estado);
    List<Proyecto> findByResponsable(String responsable);
}