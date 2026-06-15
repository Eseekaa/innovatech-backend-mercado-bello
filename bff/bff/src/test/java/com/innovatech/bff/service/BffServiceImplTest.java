package com.innovatech.bff.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
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
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

// Pruebas unitarias del BFF. RestTemplate se simula con Mockito para comprobar
// la integracion HTTP sin encender Docker ni los microservicios reales.
@ExtendWith(MockitoExtension.class)
class BffServiceImplTest {

    private static final String PROYECTOS_URL = "http://proyectos/api/proyectos";
    private static final String RECURSOS_URL = "http://recursos/api/recursos";
    private static final String TAREAS_URL = "http://tareas/api/tareas";

    @Mock
    private RestTemplate restTemplate;

    private BffServiceImpl bffService;

    @BeforeEach
    void prepararServicio() {
        bffService = new BffServiceImpl(restTemplate, PROYECTOS_URL, RECURSOS_URL, TAREAS_URL);
    }

    @Test
    void obtenerDashboardCombinaDatosYCalculaTotales() {
        BffServiceImpl servicioParcial = spy(bffService);
        ProyectoDTO activo = proyecto("ACTIVO");
        ProyectoDTO pausado = proyecto("EN_PAUSA");
        RecursoDTO disponible = recurso("DISPONIBLE");
        RecursoDTO ocupado = recurso("OCUPADO");
        TareaDTO tarea = new TareaDTO();
        TareaKpiDTO kpis = new TareaKpiDTO();

        doReturn(List.of(activo, pausado)).when(servicioParcial).obtenerProyectos();
        doReturn(List.of(disponible, ocupado)).when(servicioParcial).obtenerRecursos();
        doReturn(List.of(tarea)).when(servicioParcial).obtenerTareas();
        doReturn(kpis).when(servicioParcial).obtenerKpisTareas();

        DashboardDTO dashboard = servicioParcial.obtenerDashboard();

        assertThat(dashboard.getTotalProyectos()).isEqualTo(2);
        assertThat(dashboard.getProyectosActivos()).isEqualTo(1);
        assertThat(dashboard.getTotalRecursos()).isEqualTo(2);
        assertThat(dashboard.getRecursosDisponibles()).isEqualTo(1);
        assertThat(dashboard.getTareas()).containsExactly(tarea);
        assertThat(dashboard.getTareaKpis()).isSameAs(kpis);
    }

    @Test
    void obtenerListadosConsultaCadaMicroservicio() {
        ProyectoDTO proyecto = new ProyectoDTO();
        RecursoDTO recurso = new RecursoDTO();
        TareaDTO tarea = new TareaDTO();

        when(restTemplate.exchange(eq(PROYECTOS_URL), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(List.of(proyecto)));
        when(restTemplate.exchange(eq(RECURSOS_URL), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(List.of(recurso)));
        when(restTemplate.exchange(eq(TAREAS_URL), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(List.of(tarea)));

        assertThat(bffService.obtenerProyectos()).containsExactly(proyecto);
        assertThat(bffService.obtenerRecursos()).containsExactly(recurso);
        assertThat(bffService.obtenerTareas()).containsExactly(tarea);
    }

    @Test
    void crearProyectoAsignaVistoBuenoPendientePorDefecto() {
        ProyectoDTO nuevo = new ProyectoDTO();
        when(restTemplate.postForObject(PROYECTOS_URL, nuevo, ProyectoDTO.class)).thenReturn(nuevo);

        ProyectoDTO creado = bffService.crearProyecto(nuevo);

        assertThat(creado.getVistoBueno()).isFalse();
        verify(restTemplate).postForObject(PROYECTOS_URL, nuevo, ProyectoDTO.class);
    }

    @Test
    void crearTareaCompletaValoresPredeterminados() {
        TareaDTO nueva = new TareaDTO();
        when(restTemplate.postForObject(TAREAS_URL, nueva, TareaDTO.class)).thenReturn(nueva);

        TareaDTO creada = bffService.crearTarea(nueva);

        assertThat(creada.getEstado()).isEqualTo("PENDIENTE");
        assertThat(creada.getAvance()).isZero();
        assertThat(creada.getPrioridad()).isEqualTo("MEDIA");
    }

    @Test
    void actualizarProyectoYRecursoDevuelveLaVersionGuardada() {
        ProyectoDTO proyecto = new ProyectoDTO();
        RecursoDTO recurso = new RecursoDTO();
        when(restTemplate.getForObject(PROYECTOS_URL + "/5", ProyectoDTO.class)).thenReturn(proyecto);
        when(restTemplate.getForObject(RECURSOS_URL + "/8", RecursoDTO.class)).thenReturn(recurso);

        assertThat(bffService.actualizarProyecto(5L, proyecto)).isSameAs(proyecto);
        assertThat(bffService.actualizarRecurso(8L, recurso)).isSameAs(recurso);

        verify(restTemplate).put(PROYECTOS_URL + "/5", proyecto);
        verify(restTemplate).put(RECURSOS_URL + "/8", recurso);
    }

    @Test
    void asignarProyectoARecursoModificaSoloElProyecto() {
        RecursoDTO recurso = new RecursoDTO();
        recurso.setId(4L);
        when(restTemplate.getForObject(RECURSOS_URL + "/4", RecursoDTO.class))
                .thenReturn(recurso, recurso);

        RecursoDTO actualizado = bffService.asignarProyectoARecurso(4L, 20L);

        assertThat(actualizado.getIdProyecto()).isEqualTo(20L);
        verify(restTemplate).put(RECURSOS_URL + "/4", recurso);
    }

    @Test
    void asignarProyectoRechazaRecursoInexistente() {
        when(restTemplate.getForObject(RECURSOS_URL + "/99", RecursoDTO.class)).thenReturn(null);

        assertThatThrownBy(() -> bffService.asignarProyectoARecurso(99L, 20L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Recurso no encontrado");
    }

    @Test
    void actualizarEstadoConservaLaTareaYModificaElProgreso() {
        TareaDTO tarea = new TareaDTO();
        tarea.setTitulo("Implementar API");
        ActualizarEstadoTareaDTO cambio = new ActualizarEstadoTareaDTO();
        cambio.setEstado("EN_PROGRESO");
        cambio.setAvance(45);
        when(restTemplate.getForObject(TAREAS_URL + "/7", TareaDTO.class)).thenReturn(tarea, tarea);

        TareaDTO actualizada = bffService.actualizarEstadoTarea(7L, cambio);

        assertThat(actualizada.getEstado()).isEqualTo("EN_PROGRESO");
        assertThat(actualizada.getAvance()).isEqualTo(45);
        assertThat(actualizada.getTitulo()).isEqualTo("Implementar API");
        verify(restTemplate).put(TAREAS_URL + "/7", tarea);
    }

    @Test
    void actualizarEstadoRechazaTareaInexistente() {
        ActualizarEstadoTareaDTO cambio = new ActualizarEstadoTareaDTO();
        when(restTemplate.getForObject(TAREAS_URL + "/99", TareaDTO.class)).thenReturn(null);

        assertThatThrownBy(() -> bffService.actualizarEstadoTarea(99L, cambio))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Tarea no encontrada");
    }

    @Test
    void vistoBuenoSoloApruebaUnaTareaCompletada() {
        TareaDTO completada = new TareaDTO();
        completada.setEstado("COMPLETADA");
        when(restTemplate.getForObject(TAREAS_URL + "/3", TareaDTO.class))
                .thenReturn(completada, completada);

        TareaDTO aprobada = bffService.cambiarVistoBuenoTarea(3L, true);

        assertThat(aprobada.getVistoBueno()).isTrue();
        verify(restTemplate).put(TAREAS_URL + "/3", completada);
    }

    @Test
    void vistoBuenoRechazaTareaNoCompletada() {
        TareaDTO pendiente = new TareaDTO();
        pendiente.setEstado("PENDIENTE");
        when(restTemplate.getForObject(TAREAS_URL + "/3", TareaDTO.class)).thenReturn(pendiente);

        assertThatThrownBy(() -> bffService.cambiarVistoBuenoTarea(3L, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tareas completadas");
    }

    @Test
    void vistoBuenoRechazaTareaInexistente() {
        when(restTemplate.getForObject(TAREAS_URL + "/99", TareaDTO.class)).thenReturn(null);

        assertThatThrownBy(() -> bffService.cambiarVistoBuenoTarea(99L, true))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Tarea no encontrada");
    }

    @Test
    void consultasFiltradasYReportesUsanLasRutasCorrectas() {
        TareaDTO tarea = new TareaDTO();
        TareaKpiDTO kpis = new TareaKpiDTO();
        TareaKpiPorProyectoDTO porProyecto = new TareaKpiPorProyectoDTO();
        TareaKpiPorResponsableDTO porResponsable = new TareaKpiPorResponsableDTO();

        when(restTemplate.getForObject(TAREAS_URL + "/kpis", TareaKpiDTO.class)).thenReturn(kpis);
        when(restTemplate.getForObject(TAREAS_URL + "/11", TareaDTO.class)).thenReturn(tarea);
        when(restTemplate.exchange(eq(TAREAS_URL + "/proyecto/2"), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(List.of(tarea)));
        when(restTemplate.exchange(eq(TAREAS_URL + "/responsable/6"), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(List.of(tarea)));
        when(restTemplate.exchange(eq(TAREAS_URL + "/kpis/proyectos"), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(List.of(porProyecto)));
        when(restTemplate.exchange(eq(TAREAS_URL + "/kpis/responsables"), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok(List.of(porResponsable)));

        assertThat(bffService.obtenerKpisTareas()).isSameAs(kpis);
        assertThat(bffService.obtenerTareaPorId(11L)).isSameAs(tarea);
        assertThat(bffService.obtenerTareasPorProyecto(2L)).containsExactly(tarea);
        assertThat(bffService.obtenerTareasPorResponsable(6L)).containsExactly(tarea);
        assertThat(bffService.obtenerKpisTareasPorProyecto()).containsExactly(porProyecto);
        assertThat(bffService.obtenerKpisTareasPorResponsable()).containsExactly(porResponsable);
    }

    @Test
    void operacionesRestantesDeleganAlMicroservicioCorrespondiente() {
        TareaDTO tarea = new TareaDTO();
        when(restTemplate.getForObject(TAREAS_URL + "/9", TareaDTO.class)).thenReturn(tarea);

        assertThat(bffService.crearRecurso(new RecursoDTO())).isNull();
        assertThat(bffService.actualizarTarea(9L, tarea)).isSameAs(tarea);
        bffService.eliminarProyecto(1L);
        bffService.eliminarRecurso(2L);
        bffService.eliminarTarea(3L);

        verify(restTemplate).delete(PROYECTOS_URL + "/1");
        verify(restTemplate).delete(RECURSOS_URL + "/2");
        verify(restTemplate).delete(TAREAS_URL + "/3");
    }

    private ProyectoDTO proyecto(String estado) {
        ProyectoDTO proyecto = new ProyectoDTO();
        proyecto.setEstado(estado);
        return proyecto;
    }

    private RecursoDTO recurso(String disponibilidad) {
        RecursoDTO recurso = new RecursoDTO();
        recurso.setDisponibilidad(disponibilidad);
        return recurso;
    }
}
