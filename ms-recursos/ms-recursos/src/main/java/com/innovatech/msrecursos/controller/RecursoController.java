package com.innovatech.msrecursos.controller;

import com.innovatech.msrecursos.model.Recurso;
import com.innovatech.msrecursos.service.RecursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Controller REST que expone los endpoints del microservicio de recursos
@RestController
@RequestMapping("/api/recursos")
@CrossOrigin(origins = "*")
public class RecursoController {

    private final RecursoService recursoService;

    public RecursoController(RecursoService recursoService) {
        this.recursoService = recursoService;
    }

    // GET /api/recursos - retorna todos los empleados
    @GetMapping
    public ResponseEntity<List<Recurso>> obtenerTodos() {
        return ResponseEntity.ok(recursoService.obtenerTodos());
    }

    // GET /api/recursos/{id} - retorna un empleado por ID
    @GetMapping("/{id}")
    public ResponseEntity<Recurso> obtenerPorId(@PathVariable Long id) {
        return recursoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/recursos - crea un nuevo empleado
    @PostMapping
    public ResponseEntity<Recurso> crear(@RequestBody Recurso recurso) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recursoService.crear(recurso));
    }

    // PUT /api/recursos/{id} - actualiza un empleado
    @PutMapping("/{id}")
    public ResponseEntity<Recurso> actualizar(@PathVariable Long id,
                                              @RequestBody Recurso recurso) {
        try {
            return ResponseEntity.ok(recursoService.actualizar(id, recurso));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/recursos/{id} - elimina un empleado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        recursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/recursos/disponibilidad/{disponibilidad}
    @GetMapping("/disponibilidad/{disponibilidad}")
    public ResponseEntity<List<Recurso>> obtenerPorDisponibilidad(
            @PathVariable String disponibilidad) {
        return ResponseEntity.ok(recursoService.obtenerPorDisponibilidad(disponibilidad));
    }
}