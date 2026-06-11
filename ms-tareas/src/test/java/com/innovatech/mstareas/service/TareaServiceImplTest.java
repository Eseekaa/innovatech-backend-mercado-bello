package com.innovatech.mstareas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innovatech.mstareas.dto.ActualizarEstadoTareaRequest;
import com.innovatech.mstareas.dto.TareaKpiDTO;
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

        List<Tarea> tareas = List.of(
                tarea(EstadoTarea.PENDIENTE, 0, ayer, List.of()),
                tarea(EstadoTarea.EN_PROGRESO, 50, manana, List.of(1L)),
                tarea(EstadoTarea.COMPLETADA, 100, ayer, List.of(2L)),
                tarea(EstadoTarea.BLOQUEADA, 25, manana, List.of())
        );
        when(tareaRepository.findAll()).thenReturn(tareas);

        TareaKpiDTO kpis = tareaService.obtenerKpis();

        assertThat(kpis.getTotalTareas()).isEqualTo(4);
        assertThat(kpis.getTareasPendientes()).isEqualTo(1);
        assertThat(kpis.getTareasEnProgreso()).isEqualTo(1);
        assertThat(kpis.getTareasCompletadas()).isEqualTo(1);
        assertThat(kpis.getTareasBloqueadas()).isEqualTo(1);
        assertThat(kpis.getTareasVencidas()).isEqualTo(1);
        assertThat(kpis.getTareasSinResponsable()).isEqualTo(2);
        assertThat(kpis.getAvancePromedio()).isEqualTo(43.75);
        assertThat(kpis.getPorcentajeCompletadas()).isEqualTo(25.0);
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

    private Tarea tarea(EstadoTarea estado, int avance, LocalDate fechaFin, List<Long> responsableIds) {
        Tarea tarea = new Tarea();
        tarea.setEstado(estado);
        tarea.setAvance(avance);
        tarea.setFechaFin(fechaFin);
        tarea.setResponsableIds(responsableIds);
        return tarea;
    }
}
