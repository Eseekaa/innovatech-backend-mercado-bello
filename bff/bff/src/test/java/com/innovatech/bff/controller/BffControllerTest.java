package com.innovatech.bff.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innovatech.bff.dto.ActualizarEstadoTareaDTO;
import com.innovatech.bff.dto.DashboardDTO;
import com.innovatech.bff.dto.ProyectoDTO;
import com.innovatech.bff.dto.RecursoDTO;
import com.innovatech.bff.dto.TareaDTO;
import com.innovatech.bff.dto.TareaKpiDTO;
import com.innovatech.bff.dto.TareaKpiPorProyectoDTO;
import com.innovatech.bff.dto.TareaKpiPorResponsableDTO;
import com.innovatech.bff.service.BffService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Comprueba el contrato REST del BFF sin levantar un servidor web.
@ExtendWith(MockitoExtension.class)
class BffControllerTest {

    @Mock
    private BffService bffService;

    private BffController controller;

    @BeforeEach
    void prepararController() {
        controller = new BffController(bffService);
    }

    @Test
    void endpointsDeDashboardYProyectosRespondenCorrectamente() {
        DashboardDTO dashboard = new DashboardDTO();
        ProyectoDTO proyecto = new ProyectoDTO();
        when(bffService.obtenerDashboard()).thenReturn(dashboard);
        when(bffService.obtenerProyectos()).thenReturn(List.of(proyecto));
        when(bffService.crearProyecto(proyecto)).thenReturn(proyecto);
        when(bffService.actualizarProyecto(2L, proyecto)).thenReturn(proyecto);

        assertThat(controller.obtenerDashboard().getBody()).isSameAs(dashboard);
        assertThat(controller.obtenerProyectos().getBody()).containsExactly(proyecto);
        assertThat(controller.crearProyecto(proyecto).getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(controller.actualizarProyecto(2L, proyecto).getBody()).isSameAs(proyecto);
        assertThat(controller.eliminarProyecto(2L).getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(bffService).eliminarProyecto(2L);
    }

    @Test
    void endpointsDeRecursosDeleganTodasLasOperaciones() {
        RecursoDTO recurso = new RecursoDTO();
        when(bffService.obtenerRecursos()).thenReturn(List.of(recurso));
        when(bffService.crearRecurso(recurso)).thenReturn(recurso);
        when(bffService.actualizarRecurso(3L, recurso)).thenReturn(recurso);
        when(bffService.asignarProyectoARecurso(3L, 8L)).thenReturn(recurso);
        when(bffService.asignarProyectoARecurso(3L, null)).thenReturn(recurso);
        when(bffService.obtenerRecursosPorProyecto(8L)).thenReturn(List.of(recurso));

        assertThat(controller.obtenerRecursos().getBody()).containsExactly(recurso);
        assertThat(controller.crearRecurso(recurso).getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(controller.actualizarRecurso(3L, recurso).getBody()).isSameAs(recurso);
        assertThat(controller.asignarProyectoARecurso(3L, 8L).getBody()).isSameAs(recurso);
        assertThat(controller.quitarProyectoARecurso(3L).getBody()).isSameAs(recurso);
        assertThat(controller.obtenerRecursosPorProyecto(8L).getBody()).containsExactly(recurso);
        assertThat(controller.eliminarRecurso(3L).getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(bffService).eliminarRecurso(3L);
    }

    @Test
    void endpointsDeConsultaDeTareasYKpisRetornanLosDatosDelServicio() {
        TareaDTO tarea = new TareaDTO();
        TareaKpiDTO kpis = new TareaKpiDTO();
        TareaKpiPorProyectoDTO proyectoKpi = new TareaKpiPorProyectoDTO();
        TareaKpiPorResponsableDTO responsableKpi = new TareaKpiPorResponsableDTO();
        when(bffService.obtenerTareas()).thenReturn(List.of(tarea));
        when(bffService.obtenerKpisTareas()).thenReturn(kpis);
        when(bffService.obtenerKpisTareasPorProyecto()).thenReturn(List.of(proyectoKpi));
        when(bffService.obtenerKpisTareasPorResponsable()).thenReturn(List.of(responsableKpi));
        when(bffService.obtenerTareaPorId(5L)).thenReturn(tarea);
        when(bffService.obtenerTareasPorProyecto(8L)).thenReturn(List.of(tarea));
        when(bffService.obtenerTareasPorResponsable(12L)).thenReturn(List.of(tarea));

        assertThat(controller.obtenerTareas().getBody()).containsExactly(tarea);
        assertThat(controller.obtenerKpisTareas().getBody()).isSameAs(kpis);
        assertThat(controller.obtenerKpisTareasPorProyecto().getBody()).containsExactly(proyectoKpi);
        assertThat(controller.obtenerKpisTareasPorResponsable().getBody()).containsExactly(responsableKpi);
        assertThat(controller.obtenerTareaPorId(5L).getBody()).isSameAs(tarea);
        assertThat(controller.obtenerTareasPorProyecto(8L).getBody()).containsExactly(tarea);
        assertThat(controller.obtenerTareasPorResponsable(12L).getBody()).containsExactly(tarea);
    }

    @Test
    void endpointsDeEscrituraDeTareasMantienenEstadoYCodigosHttp() {
        TareaDTO tarea = new TareaDTO();
        ActualizarEstadoTareaDTO cambio = new ActualizarEstadoTareaDTO();
        when(bffService.crearTarea(tarea)).thenReturn(tarea);
        when(bffService.actualizarTarea(5L, tarea)).thenReturn(tarea);
        when(bffService.actualizarEstadoTarea(5L, cambio)).thenReturn(tarea);
        when(bffService.cambiarVistoBuenoTarea(5L, true)).thenReturn(tarea);

        ResponseEntity<TareaDTO> creada = controller.crearTarea(tarea);
        assertThat(creada.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(creada.getBody()).isSameAs(tarea);
        assertThat(controller.actualizarTarea(5L, tarea).getBody()).isSameAs(tarea);
        assertThat(controller.actualizarEstadoTarea(5L, cambio).getBody()).isSameAs(tarea);
        assertThat(controller.cambiarVistoBuenoTarea(5L, true).getBody()).isSameAs(tarea);
        assertThat(controller.eliminarTarea(5L).getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(bffService).eliminarTarea(5L);
    }
}
