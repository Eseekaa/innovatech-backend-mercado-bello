package com.innovatech.mstareas.controller;

import com.innovatech.mstareas.dto.ActualizarEstadoTareaRequest;
import com.innovatech.mstareas.dto.TareaKpiDTO;
import com.innovatech.mstareas.dto.TareaKpiPorProyectoDTO;
import com.innovatech.mstareas.dto.TareaKpiPorResponsableDTO;
import com.innovatech.mstareas.model.Tarea;
import com.innovatech.mstareas.service.TareaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Controller REST: expone los endpoints que usaran el BFF y el API Gateway.
@RestController
@RequestMapping("/api/tareas")
@CrossOrigin(origins = "*")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    // GET /api/tareas - lista todas las tareas.
    @GetMapping
    public ResponseEntity<List<Tarea>> obtenerTodas() {
        return ResponseEntity.ok(tareaService.obtenerTodas());
    }

    // GET /api/tareas/kpis - resumen para dashboard y reportes.
    @GetMapping("/kpis")
    public ResponseEntity<TareaKpiDTO> obtenerKpis() {
        return ResponseEntity.ok(tareaService.obtenerKpis());
    }

    // GET /api/tareas/kpis/proyectos - reporte KPI agrupado por proyecto.
    @GetMapping("/kpis/proyectos")
    public ResponseEntity<List<TareaKpiPorProyectoDTO>> obtenerKpisPorProyecto() {
        return ResponseEntity.ok(tareaService.obtenerKpisPorProyecto());
    }

    // GET /api/tareas/kpis/responsables - reporte KPI agrupado por empleado.
    @GetMapping("/kpis/responsables")
    public ResponseEntity<List<TareaKpiPorResponsableDTO>> obtenerKpisPorResponsable() {
        return ResponseEntity.ok(tareaService.obtenerKpisPorResponsable());
    }

    // GET /api/tareas/{id} - obtiene una tarea especifica.
    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtenerPorId(@PathVariable Long id) {
        return tareaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/tareas/proyecto/{proyectoId} - tareas de un proyecto.
    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<List<Tarea>> obtenerPorProyecto(@PathVariable Long proyectoId) {
        return ResponseEntity.ok(tareaService.obtenerPorProyecto(proyectoId));
    }

    // GET /api/tareas/responsable/{responsableId} - tareas asignadas a un recurso.
    @GetMapping("/responsable/{responsableId}")
    public ResponseEntity<List<Tarea>> obtenerPorResponsable(@PathVariable Long responsableId) {
        return ResponseEntity.ok(tareaService.obtenerPorResponsable(responsableId));
    }

    // POST /api/tareas - crea una nueva tarea.
    @PostMapping
    public ResponseEntity<Tarea> crear(@Valid @RequestBody Tarea tarea) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaService.crear(tarea));
    }

    // PUT /api/tareas/{id} - actualiza todos los datos editables de una tarea.
    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizar(@PathVariable Long id, @Valid @RequestBody Tarea tarea) {
        try {
            return ResponseEntity.ok(tareaService.actualizar(id, tarea));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PATCH /api/tareas/{id}/estado - cambia solo estado y avance.
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Tarea> actualizarEstado(@PathVariable Long id,
                                                  @Valid @RequestBody ActualizarEstadoTareaRequest request) {
        try {
            return ResponseEntity.ok(tareaService.actualizarEstado(id, request));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PATCH /api/tareas/{id}/visto-bueno?vistoBueno=true
    // Marca una tarea completada como aprobada formalmente por jefatura/admin.
    @PatchMapping("/{id}/visto-bueno")
    public ResponseEntity<Tarea> cambiarVistoBueno(@PathVariable Long id,
                                                   @RequestParam boolean vistoBueno) {
        try {
            return ResponseEntity.ok(tareaService.cambiarVistoBueno(id, vistoBueno));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/tareas/{id} - elimina una tarea por ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tareaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Convierte errores de reglas de negocio en respuesta HTTP 400.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> manejarReglasDeNegocio(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
