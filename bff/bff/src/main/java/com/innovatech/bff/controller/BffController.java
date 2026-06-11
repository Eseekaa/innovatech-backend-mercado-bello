package com.innovatech.bff.controller;

import com.innovatech.bff.dto.DashboardDTO;
import com.innovatech.bff.dto.ProyectoDTO;
import com.innovatech.bff.dto.RecursoDTO;
import com.innovatech.bff.dto.TareaDTO;
import com.innovatech.bff.dto.TareaKpiDTO;
import com.innovatech.bff.dto.TareaKpiPorProyectoDTO;
import com.innovatech.bff.dto.TareaKpiPorResponsableDTO;
import com.innovatech.bff.dto.ActualizarEstadoTareaDTO;
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

    // PATCH /api/bff/recursos/{id}/proyecto/{idProyecto}
    // Asigna un empleado a un proyecto pasando por el BFF.
    @PatchMapping("/recursos/{id}/proyecto/{idProyecto}")
    public ResponseEntity<RecursoDTO> asignarProyectoARecurso(@PathVariable Long id,
                                                              @PathVariable Long idProyecto) {
        return ResponseEntity.ok(bffService.asignarProyectoARecurso(id, idProyecto));
    }

    // PATCH /api/bff/recursos/{id}/proyecto
    // Deja al empleado sin proyecto asignado.
    @PatchMapping("/recursos/{id}/proyecto")
    public ResponseEntity<RecursoDTO> quitarProyectoARecurso(@PathVariable Long id) {
        return ResponseEntity.ok(bffService.asignarProyectoARecurso(id, null));
    }

    // DELETE /api/bff/recursos/{id}
    @DeleteMapping("/recursos/{id}")
    public ResponseEntity<Void> eliminarRecurso(@PathVariable Long id) {
        bffService.eliminarRecurso(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/bff/recursos/proyecto/{idProyecto}
    // Retorna todos los recursos asignados a un proyecto específico
    // Permite ver qué empleados están trabajando en un proyecto
    @GetMapping("/recursos/proyecto/{idProyecto}")
    public ResponseEntity<List<RecursoDTO>> obtenerRecursosPorProyecto(@PathVariable Long idProyecto) {
        return ResponseEntity.ok(bffService.obtenerRecursosPorProyecto(idProyecto));
    }

    // GET /api/bff/tareas - lista todas las tareas desde ms-tareas.
    @GetMapping("/tareas")
    public ResponseEntity<List<TareaDTO>> obtenerTareas() {
        return ResponseEntity.ok(bffService.obtenerTareas());
    }

    // GET /api/bff/tareas/kpis - resumen de tareas para dashboard/reportes.
    @GetMapping("/tareas/kpis")
    public ResponseEntity<TareaKpiDTO> obtenerKpisTareas() {
        return ResponseEntity.ok(bffService.obtenerKpisTareas());
    }

    // GET /api/bff/tareas/kpis/proyectos - KPIs agrupados por proyecto.
    @GetMapping("/tareas/kpis/proyectos")
    public ResponseEntity<List<TareaKpiPorProyectoDTO>> obtenerKpisTareasPorProyecto() {
        return ResponseEntity.ok(bffService.obtenerKpisTareasPorProyecto());
    }

    // GET /api/bff/tareas/kpis/responsables - KPIs agrupados por empleado.
    @GetMapping("/tareas/kpis/responsables")
    public ResponseEntity<List<TareaKpiPorResponsableDTO>> obtenerKpisTareasPorResponsable() {
        return ResponseEntity.ok(bffService.obtenerKpisTareasPorResponsable());
    }

    // GET /api/bff/tareas/{id} - obtiene una tarea especifica.
    @GetMapping("/tareas/{id}")
    public ResponseEntity<TareaDTO> obtenerTareaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bffService.obtenerTareaPorId(id));
    }

    // GET /api/bff/tareas/proyecto/{proyectoId} - tareas de un proyecto.
    @GetMapping("/tareas/proyecto/{proyectoId}")
    public ResponseEntity<List<TareaDTO>> obtenerTareasPorProyecto(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(bffService.obtenerTareasPorProyecto(proyectoId));
    }

    // GET /api/bff/tareas/responsable/{responsableId} - tareas asignadas a un empleado.
    @GetMapping("/tareas/responsable/{responsableId}")
    public ResponseEntity<List<TareaDTO>> obtenerTareasPorResponsable(@PathVariable Long responsableId) {
        return ResponseEntity.ok(bffService.obtenerTareasPorResponsable(responsableId));
    }

    // POST /api/bff/tareas - crea una tarea usando el BFF.
    @PostMapping("/tareas")
    public ResponseEntity<TareaDTO> crearTarea(@RequestBody TareaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bffService.crearTarea(dto));
    }

    // PUT /api/bff/tareas/{id} - edita una tarea completa.
    @PutMapping("/tareas/{id}")
    public ResponseEntity<TareaDTO> actualizarTarea(@PathVariable Long id, @RequestBody TareaDTO dto) {
        return ResponseEntity.ok(bffService.actualizarTarea(id, dto));
    }

    // PATCH /api/bff/tareas/{id}/estado - cambia solo estado y avance.
    @PatchMapping("/tareas/{id}/estado")
    public ResponseEntity<TareaDTO> actualizarEstadoTarea(@PathVariable Long id,
                                                          @RequestBody ActualizarEstadoTareaDTO dto) {
        return ResponseEntity.ok(bffService.actualizarEstadoTarea(id, dto));
    }

    // DELETE /api/bff/tareas/{id} - elimina una tarea.
    @DeleteMapping("/tareas/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        bffService.eliminarTarea(id);
        return ResponseEntity.noContent().build();
    }
}
