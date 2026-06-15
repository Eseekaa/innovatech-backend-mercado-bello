package com.innovatech.mstareas.repository;

import com.innovatech.mstareas.model.EstadoTarea;
import com.innovatech.mstareas.model.Tarea;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// Repository Pattern: Spring Data crea las consultas CRUD automaticamente.
@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByProyectoId(Long proyectoId);

    List<Tarea> findByEstado(EstadoTarea estado);

    long countByEstado(EstadoTarea estado);

    // Consulta la tabla intermedia tarea_responsables creada por @ElementCollection.
    @Query("select t from Tarea t join t.responsableIds responsableId where responsableId = :responsableId")
    List<Tarea> findByResponsableId(@Param("responsableId") Long responsableId);
}
