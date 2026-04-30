package com.innovatech.msrecursos.service;

import com.innovatech.msrecursos.model.Recurso;
import java.util.List;
import java.util.Optional;

// Interfaz que define el contrato del servicio de recursos humanos
public interface RecursoService {
    List<Recurso> obtenerTodos();
    Optional<Recurso> obtenerPorId(Long id);
    Recurso crear(Recurso recurso);
    Recurso actualizar(Long id, Recurso recurso);
    void eliminar(Long id);
    List<Recurso> obtenerPorDisponibilidad(String disponibilidad);
}