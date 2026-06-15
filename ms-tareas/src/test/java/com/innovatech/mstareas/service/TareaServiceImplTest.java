package com.innovatech.mstareas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innovatech.mstareas.dto.ActualizarEstadoTareaRequest;
import com.innovatech.mstareas.dto.TareaKpiDTO;
import com.innovatech.mstareas.dto.TareaKpiPorProyectoDTO;
import com.innovatech.mstareas.dto.TareaKpiPorResponsableDTO;
import com.innovatech.mstareas.model.EstadoTarea;
import com.innovatech.mstareas.model.Tarea;
import com.innovatech.mstareas.repository.TareaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Pruebas unitarias de la logica de negocio de tareas.
// Mockito reemplaza el repository para probar el servicio sin levantar base de datos.
@ExtendWith(MockitoExtension.class)
class TareaServiceImplTest {

    @Mock
    private TareaRepository tareaRepository;

    @InjectMocks
    private TareaServiceImpl tareaService;

    @Test
    void obtenerKpisCalculaResumenDeTareas() {
        LocalDate ayer = LocalDate.now().minusDays(1);
        LocalDate manana = LocalDate.now().plusDays(1);

        Tarea tareaCompletadaAprobada = tarea(EstadoTarea.COMPLETADA, 100, ayer, List.of(2L));
        tareaCompletadaAprobada.setVistoBueno(true);
        Tarea tareaCompletadaPendienteAprobacion = tarea(EstadoTarea.COMPLETADA, 100, manana, List.of(3L));

        List<Tarea> tareas = List.of(
                tarea(EstadoTarea.PENDIENTE, 0, ayer, List.of()),
                tarea(EstadoTarea.EN_PROGRESO, 50, manana, List.of(1L)),
                tareaCompletadaAprobada,
                tareaCompletadaPendienteAprobacion,
                tarea(EstadoTarea.BLOQUEADA, 25, manana, List.of())
        );
        when(tareaRepository.findAll()).thenReturn(tareas);

        TareaKpiDTO kpis = tareaService.obtenerKpis();

        assertThat(kpis.getTotalTareas()).isEqualTo(5);
        assertThat(kpis.getTareasPendientes()).isEqualTo(1);
        assertThat(kpis.getTareasEnProgreso()).isEqualTo(1);
        assertThat(kpis.getTareasCompletadas()).isEqualTo(2);
        assertThat(kpis.getTareasAprobadas()).isEqualTo(1);
        assertThat(kpis.getTareasPendientesAprobacion()).isEqualTo(1);
        assertThat(kpis.getTareasBloqueadas()).isEqualTo(1);
        assertThat(kpis.getTareasVencidas()).isEqualTo(1);
        assertThat(kpis.getTareasSinResponsable()).isEqualTo(2);
        assertThat(kpis.getAvancePromedio()).isEqualTo(55.0);
        assertThat(kpis.getPorcentajeCompletadas()).isEqualTo(40.0);
        verify(tareaRepository).findAll();
    }

    @Test
    void obtenerKpisSinTareasDevuelveCeros() {
        when(tareaRepository.findAll()).thenReturn(List.of());

        TareaKpiDTO kpis = tareaService.obtenerKpis();

        assertThat(kpis.getTotalTareas()).isZero();
        assertThat(kpis.getAvancePromedio()).isZero();
        assertThat(kpis.getPorcentajeCompletadas()).isZero();
        verify(tareaRepository).findAll();
    }

    @Test
    void actualizarEstadoBloqueadoNoCuentaLaTareaComoCompletada() {
        // Caso real: una tarea que estaba al 100% puede quedar bloqueada por
        // una aprobacion o dependencia externa. El sistema no debe dejarla
        // como COMPLETADA si el usuario eligio BLOQUEADA.
        Tarea tareaExistente = tarea(EstadoTarea.COMPLETADA, 100, LocalDate.now().plusDays(1), List.of(1L));
        ActualizarEstadoTareaRequest request = new ActualizarEstadoTareaRequest();
        request.setEstado(EstadoTarea.BLOQUEADA);
        request.setAvance(100);

        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tareaExistente));
        when(tareaRepository.save(any(Tarea.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tarea actualizada = tareaService.actualizarEstado(1L, request);

        assertThat(actualizada.getEstado()).isEqualTo(EstadoTarea.BLOQUEADA);
        assertThat(actualizada.getAvance()).isEqualTo(99);
        verify(tareaRepository).findById(1L);
        verify(tareaRepository).save(tareaExistente);
    }

    @Test
    void cambiarVistoBuenoSoloPermiteAprobarTareasCompletadas() {
        Tarea tareaCompletada = tarea(EstadoTarea.COMPLETADA, 100, LocalDate.now().plusDays(1), List.of(1L));

        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tareaCompletada));
        when(tareaRepository.save(any(Tarea.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tarea aprobada = tareaService.cambiarVistoBueno(1L, true);

        assertThat(aprobada.getVistoBueno()).isTrue();
        verify(tareaRepository).findById(1L);
        verify(tareaRepository).save(tareaCompletada);
    }

    @Test
    void cambiarVistoBuenoRechazaTareasNoCompletadas() {
        Tarea tareaEnProgreso = tarea(EstadoTarea.EN_PROGRESO, 50, LocalDate.now().plusDays(1), List.of(1L));

        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tareaEnProgreso));

        assertThatThrownBy(() -> tareaService.cambiarVistoBueno(1L, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Solo se puede dar visto bueno");

        verify(tareaRepository).findById(1L);
    }

    @Test
    void obtenerKpisPorProyectoAgrupaTareasPorProyecto() {
        Tarea tareaProyecto10 = tarea(EstadoTarea.EN_PROGRESO, 50, LocalDate.now().plusDays(1), List.of(1L));
        tareaProyecto10.setProyectoId(10L);
        Tarea tareaCompletadaProyecto10 = tarea(EstadoTarea.COMPLETADA, 100, LocalDate.now().plusDays(1), List.of(2L));
        tareaCompletadaProyecto10.setProyectoId(10L);
        Tarea tareaProyecto20 = tarea(EstadoTarea.PENDIENTE, 0, LocalDate.now().plusDays(1), List.of());
        tareaProyecto20.setProyectoId(20L);

        when(tareaRepository.findAll()).thenReturn(List.of(tareaProyecto10, tareaCompletadaProyecto10, tareaProyecto20));

        List<TareaKpiPorProyectoDTO> reportes = tareaService.obtenerKpisPorProyecto();

        assertThat(reportes).hasSize(2);
        assertThat(reportes.get(0).getProyectoId()).isEqualTo(10L);
        assertThat(reportes.get(0).getTotalTareas()).isEqualTo(2);
        assertThat(reportes.get(0).getTareasCompletadas()).isEqualTo(1);
        assertThat(reportes.get(0).getAvancePromedio()).isEqualTo(75.0);
        assertThat(reportes.get(1).getProyectoId()).isEqualTo(20L);
        assertThat(reportes.get(1).getTareasSinResponsable()).isEqualTo(1);
        verify(tareaRepository).findAll();
    }

    @Test
    void obtenerKpisPorResponsableCuentaLaMismaTareaParaCadaResponsableAsignado() {
        Tarea tareaCompartida = tarea(EstadoTarea.EN_PROGRESO, 50, LocalDate.now().plusDays(1), List.of(1L, 2L));
        Tarea tareaSoloResponsable1 = tarea(EstadoTarea.COMPLETADA, 100, LocalDate.now().plusDays(1), List.of(1L));
        Tarea tareaSinResponsable = tarea(EstadoTarea.PENDIENTE, 0, LocalDate.now().plusDays(1), List.of());

        when(tareaRepository.findAll()).thenReturn(List.of(tareaCompartida, tareaSoloResponsable1, tareaSinResponsable));

        List<TareaKpiPorResponsableDTO> reportes = tareaService.obtenerKpisPorResponsable();

        assertThat(reportes).hasSize(2);
        assertThat(reportes.get(0).getResponsableId()).isEqualTo(1L);
        assertThat(reportes.get(0).getTotalTareas()).isEqualTo(2);
        assertThat(reportes.get(0).getTareasCompletadas()).isEqualTo(1);
        assertThat(reportes.get(0).getAvancePromedio()).isEqualTo(75.0);
        assertThat(reportes.get(1).getResponsableId()).isEqualTo(2L);
        assertThat(reportes.get(1).getTotalTareas()).isEqualTo(1);
        assertThat(reportes.get(1).getTareasEnProgreso()).isEqualTo(1);
        verify(tareaRepository).findAll();
    }

    private Tarea tarea(EstadoTarea estado, int avance, LocalDate fechaFin, List<Long> responsableIds) {
        Tarea tarea = new Tarea();
        tarea.setEstado(estado);
        tarea.setAvance(avance);
        tarea.setFechaFin(fechaFin);
        tarea.setResponsableIds(responsableIds);
        return tarea;
    }
}
