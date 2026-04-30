package com.innovatech.msproyectos.controller;

import com.innovatech.msproyectos.model.Proyecto;
import com.innovatech.msproyectos.service.ProyectoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Controller REST que expone los endpoints del microservicio de proyectos
// El BFF llama a estos endpoints
@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "*")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    // GET /api/proyectos - retorna todos los proyectos
    @GetMapping
    public ResponseEntity<List<Proyecto>> obtenerTodos() {
        return ResponseEntity.ok(proyectoService.obtenerTodos());
    }

    // GET /api/proyectos/{id} - retorna un proyecto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> obtenerPorId(@PathVariable Long id) {
        return proyectoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/proyectos - crea un nuevo proyecto
    @PostMapping
    public ResponseEntity<Proyecto> crear(@RequestBody Proyecto proyecto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(proyectoService.crear(proyecto));
    }

    // PUT /api/proyectos/{id} - actualiza un proyecto existente
    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> actualizar(@PathVariable Long id,
                                               @RequestBody Proyecto proyecto) {
        try {
            return ResponseEntity.ok(proyectoService.actualizar(id, proyecto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/proyectos/{id} - elimina un proyecto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        proyectoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/proyectos/estado/{estado} - filtra por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Proyecto>> obtenerPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(proyectoService.obtenerPorEstado(estado));
    }
}