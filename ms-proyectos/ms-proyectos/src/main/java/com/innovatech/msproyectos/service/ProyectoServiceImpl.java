package com.innovatech.msproyectos.service;

import com.innovatech.msproyectos.model.Proyecto;
import com.innovatech.msproyectos.repository.ProyectoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Implementación real del servicio de proyectos
// Patrón Dependency Injection: Spring inyecta el repositorio
@Service
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepository;

    public ProyectoServiceImpl(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.findAll();
    }

    @Override
    public Optional<Proyecto> obtenerPorId(Long id) {
        return proyectoRepository.findById(id);
    }

    @Override
    public Proyecto crear(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    public Proyecto actualizar(Long id, Proyecto proyectoActualizado) {
        Proyecto existente = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado: " + id));
        existente.setNombre(proyectoActualizado.getNombre());
        existente.setDescripcion(proyectoActualizado.getDescripcion());
        existente.setEstado(proyectoActualizado.getEstado());
        existente.setFechaInicio(proyectoActualizado.getFechaInicio());
        existente.setFechaFin(proyectoActualizado.getFechaFin());
        existente.setResponsable(proyectoActualizado.getResponsable());
        return proyectoRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        proyectoRepository.deleteById(id);
    }

    @Override
    public List<Proyecto> obtenerPorEstado(String estado) {
        return proyectoRepository.findByEstado(estado);
    }
}