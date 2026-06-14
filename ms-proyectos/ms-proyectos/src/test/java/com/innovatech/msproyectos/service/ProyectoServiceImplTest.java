package com.innovatech.msproyectos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innovatech.msproyectos.model.Proyecto;
import com.innovatech.msproyectos.repository.ProyectoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Pruebas unitarias de la logica de negocio de proyectos.
// Mockito reemplaza el repository para probar el servicio sin iniciar H2 ni Spring.
@ExtendWith(MockitoExtension.class)
class ProyectoServiceImplTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @InjectMocks
    private ProyectoServiceImpl proyectoService;

    @Test
    void obtenerTodosDevuelveLosProyectosDelRepository() {
        List<Proyecto> proyectos = List.of(proyecto("Portal web"), proyecto("Aplicacion movil"));
        when(proyectoRepository.findAll()).thenReturn(proyectos);

        List<Proyecto> resultado = proyectoService.obtenerTodos();

        assertThat(resultado).containsExactlyElementsOf(proyectos);
        verify(proyectoRepository).findAll();
    }

    @Test
    void obtenerPorIdDevuelveElProyectoEncontrado() {
        Proyecto proyecto = proyecto("Portal web");
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        Optional<Proyecto> resultado = proyectoService.obtenerPorId(1L);

        assertThat(resultado).contains(proyecto);
        verify(proyectoRepository).findById(1L);
    }

    @Test
    void crearAsignaVistoBuenoPendienteCuandoNoFueEnviado() {
        Proyecto proyecto = proyecto("Portal web");
        proyecto.setVistoBueno(null);
        when(proyectoRepository.save(proyecto)).thenReturn(proyecto);

        Proyecto creado = proyectoService.crear(proyecto);

        assertThat(creado.getVistoBueno()).isFalse();
        verify(proyectoRepository).save(proyecto);
    }

    @Test
    void crearMantieneVistoBuenoCuandoFueEnviado() {
        Proyecto proyecto = proyecto("Portal web");
        proyecto.setVistoBueno(true);
        when(proyectoRepository.save(proyecto)).thenReturn(proyecto);

        Proyecto creado = proyectoService.crear(proyecto);

        assertThat(creado.getVistoBueno()).isTrue();
        verify(proyectoRepository).save(proyecto);
    }

    @Test
    void actualizarCopiaLosDatosYMantieneVistoBuenoSiNoFueEnviado() {
        Proyecto existente = proyecto("Nombre anterior");
        existente.setVistoBueno(true);
        Proyecto cambios = proyecto("Nombre actualizado");
        cambios.setDescripcion("Descripcion actualizada");
        cambios.setEstado("EN_PAUSA");
        cambios.setFechaInicio(LocalDate.of(2026, 6, 1));
        cambios.setFechaFin(LocalDate.of(2026, 6, 30));
        cambios.setResponsable("Matias Mercado");
        cambios.setVistoBueno(null);

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(proyectoRepository.save(existente)).thenReturn(existente);

        Proyecto actualizado = proyectoService.actualizar(1L, cambios);

        assertThat(actualizado.getNombre()).isEqualTo("Nombre actualizado");
        assertThat(actualizado.getDescripcion()).isEqualTo("Descripcion actualizada");
        assertThat(actualizado.getEstado()).isEqualTo("EN_PAUSA");
        assertThat(actualizado.getFechaInicio()).isEqualTo(LocalDate.of(2026, 6, 1));
        assertThat(actualizado.getFechaFin()).isEqualTo(LocalDate.of(2026, 6, 30));
        assertThat(actualizado.getResponsable()).isEqualTo("Matias Mercado");
        assertThat(actualizado.getVistoBueno()).isTrue();
        verify(proyectoRepository).save(existente);
    }

    @Test
    void actualizarCambiaVistoBuenoCuandoFueEnviado() {
        Proyecto existente = proyecto("Portal web");
        existente.setVistoBueno(true);
        Proyecto cambios = proyecto("Portal web actualizado");
        cambios.setVistoBueno(false);

        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(proyectoRepository.save(existente)).thenReturn(existente);

        Proyecto actualizado = proyectoService.actualizar(1L, cambios);

        assertThat(actualizado.getVistoBueno()).isFalse();
        verify(proyectoRepository).save(existente);
    }

    @Test
    void actualizarLanzaExcepcionCuandoElProyectoNoExiste() {
        when(proyectoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> proyectoService.actualizar(99L, proyecto("Inexistente")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Proyecto no encontrado: 99");

        verify(proyectoRepository).findById(99L);
    }

    @Test
    void eliminarDelegaLaOperacionAlRepository() {
        proyectoService.eliminar(1L);

        verify(proyectoRepository).deleteById(1L);
    }

    @Test
    void obtenerPorEstadoDevuelveSoloLosProyectosSolicitados() {
        List<Proyecto> proyectosActivos = List.of(proyecto("Portal web"));
        when(proyectoRepository.findByEstado("ACTIVO")).thenReturn(proyectosActivos);

        List<Proyecto> resultado = proyectoService.obtenerPorEstado("ACTIVO");

        assertThat(resultado).containsExactlyElementsOf(proyectosActivos);
        verify(proyectoRepository).findByEstado("ACTIVO");
    }

    private Proyecto proyecto(String nombre) {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(nombre);
        proyecto.setDescripcion("Descripcion de prueba");
        proyecto.setEstado("ACTIVO");
        proyecto.setVistoBueno(false);
        return proyecto;
    }
}
