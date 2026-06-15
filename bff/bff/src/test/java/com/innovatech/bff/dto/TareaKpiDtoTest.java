package com.innovatech.bff.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

// Verifica el contrato de las metricas transportadas desde ms-tareas a React.
class TareaKpiDtoTest {

    @Test
    void tareaKpiDtoConservaTodasLasMetricas() {
        TareaKpiDTO dto = new TareaKpiDTO();
        dto.setTotalTareas(10);
        dto.setTareasPendientes(1);
        dto.setTareasEnProgreso(2);
        dto.setTareasCompletadas(3);
        dto.setTareasAprobadas(2);
        dto.setTareasPendientesAprobacion(1);
        dto.setTareasBloqueadas(1);
        dto.setTareasVencidas(2);
        dto.setTareasSinResponsable(1);
        dto.setAvancePromedio(64.5);
        dto.setPorcentajeCompletadas(30.0);

        assertThat(dto.getTotalTareas()).isEqualTo(10);
        assertThat(dto.getTareasPendientes()).isEqualTo(1);
        assertThat(dto.getTareasEnProgreso()).isEqualTo(2);
        assertThat(dto.getTareasCompletadas()).isEqualTo(3);
        assertThat(dto.getTareasAprobadas()).isEqualTo(2);
        assertThat(dto.getTareasPendientesAprobacion()).isEqualTo(1);
        assertThat(dto.getTareasBloqueadas()).isEqualTo(1);
        assertThat(dto.getTareasVencidas()).isEqualTo(2);
        assertThat(dto.getTareasSinResponsable()).isEqualTo(1);
        assertThat(dto.getAvancePromedio()).isEqualTo(64.5);
        assertThat(dto.getPorcentajeCompletadas()).isEqualTo(30.0);
    }

    @Test
    void tareaKpiPorProyectoDtoConservaElDesglose() {
        TareaKpiPorProyectoDTO dto = new TareaKpiPorProyectoDTO();
        dto.setProyectoId(7L);
        dto.setTotalTareas(8);
        dto.setTareasPendientes(1);
        dto.setTareasEnProgreso(2);
        dto.setTareasCompletadas(3);
        dto.setTareasAprobadas(2);
        dto.setTareasPendientesAprobacion(1);
        dto.setTareasBloqueadas(1);
        dto.setTareasVencidas(2);
        dto.setTareasSinResponsable(1);
        dto.setAvancePromedio(70.0);
        dto.setPorcentajeCompletadas(37.5);

        assertThat(dto.getProyectoId()).isEqualTo(7L);
        assertThat(dto.getTotalTareas()).isEqualTo(8);
        assertThat(dto.getTareasPendientes()).isEqualTo(1);
        assertThat(dto.getTareasEnProgreso()).isEqualTo(2);
        assertThat(dto.getTareasCompletadas()).isEqualTo(3);
        assertThat(dto.getTareasAprobadas()).isEqualTo(2);
        assertThat(dto.getTareasPendientesAprobacion()).isEqualTo(1);
        assertThat(dto.getTareasBloqueadas()).isEqualTo(1);
        assertThat(dto.getTareasVencidas()).isEqualTo(2);
        assertThat(dto.getTareasSinResponsable()).isEqualTo(1);
        assertThat(dto.getAvancePromedio()).isEqualTo(70.0);
        assertThat(dto.getPorcentajeCompletadas()).isEqualTo(37.5);
    }

    @Test
    void tareaKpiPorResponsableDtoConservaElDesglose() {
        TareaKpiPorResponsableDTO dto = new TareaKpiPorResponsableDTO();
        dto.setResponsableId(4L);
        dto.setTotalTareas(6);
        dto.setTareasPendientes(1);
        dto.setTareasEnProgreso(2);
        dto.setTareasCompletadas(3);
        dto.setTareasAprobadas(2);
        dto.setTareasPendientesAprobacion(1);
        dto.setTareasBloqueadas(1);
        dto.setTareasVencidas(2);
        dto.setAvancePromedio(75.0);
        dto.setPorcentajeCompletadas(50.0);

        assertThat(dto.getResponsableId()).isEqualTo(4L);
        assertThat(dto.getTotalTareas()).isEqualTo(6);
        assertThat(dto.getTareasPendientes()).isEqualTo(1);
        assertThat(dto.getTareasEnProgreso()).isEqualTo(2);
        assertThat(dto.getTareasCompletadas()).isEqualTo(3);
        assertThat(dto.getTareasAprobadas()).isEqualTo(2);
        assertThat(dto.getTareasPendientesAprobacion()).isEqualTo(1);
        assertThat(dto.getTareasBloqueadas()).isEqualTo(1);
        assertThat(dto.getTareasVencidas()).isEqualTo(2);
        assertThat(dto.getAvancePromedio()).isEqualTo(75.0);
        assertThat(dto.getPorcentajeCompletadas()).isEqualTo(50.0);
    }
}
