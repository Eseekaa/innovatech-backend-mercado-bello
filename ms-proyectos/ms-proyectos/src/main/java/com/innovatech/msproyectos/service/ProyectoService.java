package com.innovatech.msproyectos.service;

import com.innovatech.msproyectos.model.Proyecto;
import java.util.List;
import java.util.Optional;

// Interfaz que define el contrato del servicio
// Patrón Strategy: define QUÉ hace sin implementarlo
public interface ProyectoService {
    List<Proyecto> obtenerTodos();
    Optional<Proyecto> obtenerPorId(Long id);
    Proyecto crear(Proyecto proyecto);
    Proyecto actualizar(Long id, Proyecto proyecto);
    void eliminar(Long id);
    List<Proyecto> obtenerPorEstado(String estado);
}