package com.innovatech.bff.controller;

import com.innovatech.bff.dto.DashboardDTO;
import com.innovatech.bff.dto.ProyectoDTO;
import com.innovatech.bff.dto.RecursoDTO;
import com.innovatech.bff.service.BffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Controller REST - único punto de contacto entre React y el backend
// El frontend NUNCA llama directamente a los microservicios
@RestController
@RequestMapping("/api/bff")
@CrossOrigin(origins = "*")
public class BffController {

    private final BffService bffService;

    public BffController(BffService bffService) {
        this.bffService = bffService;
    }

    // GET /api/bff/dashboard - datos combinados de proyectos y recursos
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> obtenerDashboard() {
        return ResponseEntity.ok(bffService.obtenerDashboard());
    }

    // GET /api/bff/proyectos
    @GetMapping("/proyectos")
    public ResponseEntity<List<ProyectoDTO>> obtenerProyectos() {
        return ResponseEntity.ok(bffService.obtenerProyectos());
    }

    // POST /api/bff/proyectos
    @PostMapping("/proyectos")
    public ResponseEntity<ProyectoDTO> crearProyecto(@RequestBody ProyectoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bffService.crearProyecto(dto));
    }

    // PUT /api/bff/proyectos/{id}
    @PutMapping("/proyectos/{id}")
    public ResponseEntity<ProyectoDTO> actualizarProyecto(@PathVariable Long id,
                                                           @RequestBody ProyectoDTO dto) {
        return ResponseEntity.ok(bffService.actualizarProyecto(id, dto));
    }

    // DELETE /api/bff/proyectos/{id}
    @DeleteMapping("/proyectos/{id}")
    public ResponseEntity<Void> eliminarProyecto(@PathVariable Long id) {
        bffService.eliminarProyecto(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/bff/recursos
    @GetMapping("/recursos")
    public ResponseEntity<List<RecursoDTO>> obtenerRecursos() {
        return ResponseEntity.ok(bffService.obtenerRecursos());
    }

    // POST /api/bff/recursos
    @PostMapping("/recursos")
    public ResponseEntity<RecursoDTO> crearRecurso(@RequestBody RecursoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bffService.crearRecurso(dto));
    }

    // PUT /api/bff/recursos/{id}
    @PutMapping("/recursos/{id}")
    public ResponseEntity<RecursoDTO> actualizarRecurso(@PathVariable Long id,
                                                         @RequestBody RecursoDTO dto) {
        return ResponseEntity.ok(bffService.actualizarRecurso(id, dto));
    }

    // DELETE /api/bff/recursos/{id}
    @DeleteMapping("/recursos/{id}")
    public ResponseEntity<Void> eliminarRecurso(@PathVariable Long id) {
        bffService.eliminarRecurso(id);
        return ResponseEntity.noContent().build();
    }
}