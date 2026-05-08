package com.innovatech.msrecursos.service;

import com.innovatech.msrecursos.model.Recurso;
import com.innovatech.msrecursos.repository.RecursoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Implementacion real del servicio de recursos humanos
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
        normalizarProyectos(recurso);
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
        // Actualiza la relacion con uno o varios proyectos.
        normalizarProyectos(recursoActualizado);
        existente.setIdProyectos(recursoActualizado.getIdProyectos());
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

    @Override
    public List<Recurso> obtenerPorProyecto(Long idProyecto) {
        // Retorna recursos asignados al proyecto, incluso si tienen varios proyectos.
        return recursoRepository.findAll().stream()
                .filter(r -> r.getIdProyectos() != null && r.getIdProyectos().contains(idProyecto))
                .toList();
    }

    @Override
    public Recurso asignarProyecto(Long id, Long idProyecto) {
        Recurso existente = recursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado: " + id));
        // Solo cambia la relacion con proyecto; los datos personales quedan intactos.
        if (idProyecto == null) {
            existente.setIdProyectos(List.of());
        } else {
            existente.setIdProyectos(List.of(idProyecto));
        }
        return recursoRepository.save(existente);
    }

    private void normalizarProyectos(Recurso recurso) {
        // Compatibilidad: si llega idProyecto antiguo, lo transforma en lista.
        if ((recurso.getIdProyectos() == null || recurso.getIdProyectos().isEmpty())
                && recurso.getIdProyecto() != null) {
            recurso.setIdProyectos(List.of(recurso.getIdProyecto()));
        } else {
            recurso.setIdProyectos(recurso.getIdProyectos());
        }
    }
}
