package com.innovatech.msrecursos.repository;

import com.innovatech.msrecursos.model.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Repository Pattern: maneja acceso a datos de recursos humanos
@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {
    List<Recurso> findByDepartamento(String departamento);
    List<Recurso> findByDisponibilidad(String disponibilidad);
    List<Recurso> findByCargo(String cargo);
    // Buscar recursos por proyecto asignado
    List<Recurso> findByIdProyecto(Long idProyecto);
}