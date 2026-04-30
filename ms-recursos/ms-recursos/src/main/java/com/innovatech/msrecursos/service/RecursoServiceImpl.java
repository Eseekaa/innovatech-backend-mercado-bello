package com.innovatech.msrecursos.service;

import com.innovatech.msrecursos.model.Recurso;
import com.innovatech.msrecursos.repository.RecursoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Implementación real del servicio de recursos humanos
@Service
public class RecursoServiceImpl implements RecursoService {

    private final RecursoRepository recursoRepository;

    public RecursoServiceImpl(RecursoRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }

    @Override
    public List<Recurso> obtenerTodos() {
        return recursoRepository.findAll();
    }

    @Override
    public Optional<Recurso> obtenerPorId(Long id) {
        return recursoRepository.findById(id);
    }

    @Override
    public Recurso crear(Recurso recurso) {
        return recursoRepository.save(recurso);
    }

    @Override
    public Recurso actualizar(Long id, Recurso recursoActualizado) {
        Recurso existente = recursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado: " + id));
        existente.setNombre(recursoActualizado.getNombre());
        existente.setApellido(recursoActualizado.getApellido());
        existente.setEmail(recursoActualizado.getEmail());
        existente.setCargo(recursoActualizado.getCargo());
        existente.setDepartamento(recursoActualizado.getDepartamento());
        existente.setDisponibilidad(recursoActualizado.getDisponibilidad());
        existente.setNivelExperiencia(recursoActualizado.getNivelExperiencia());
        return recursoRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        recursoRepository.deleteById(id);
    }

    @Override
    public List<Recurso> obtenerPorDisponibilidad(String disponibilidad) {
        return recursoRepository.findByDisponibilidad(disponibilidad);
    }
}