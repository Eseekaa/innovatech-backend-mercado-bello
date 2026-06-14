package com.innovatech.msrecursos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.innovatech.msrecursos.model.Recurso;
import com.innovatech.msrecursos.repository.RecursoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Pruebas unitarias de las reglas de gestion de recursos humanos.
// El repository es un mock: no se inicia Spring ni la base de datos H2.
@ExtendWith(MockitoExtension.class)
class RecursoServiceImplTest {

    @Mock
    private RecursoRepository recursoRepository;

    @InjectMocks
    private RecursoServiceImpl recursoService;

    @Test
    void obtenerTodosDevuelveLosRecursosDelRepository() {
        List<Recurso> recursos = List.of(recurso("Ana"), recurso("Luis"));
        when(recursoRepository.findAll()).thenReturn(recursos);

        List<Recurso> resultado = recursoService.obtenerTodos();

        assertThat(resultado).containsExactlyElementsOf(recursos);
        verify(recursoRepository).findAll();
    }

    @Test
    void obtenerPorIdDevuelveElRecursoEncontrado() {
        Recurso recurso = recurso("Ana");
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(recurso));

        Optional<Recurso> resultado = recursoService.obtenerPorId(1L);

        assertThat(resultado).contains(recurso);
        verify(recursoRepository).findById(1L);
    }

    @Test
    void crearMantieneVariosProyectosAsignados() {
        Recurso recurso = recurso("Ana");
        recurso.setIdProyectos(List.of(10L, 20L));
        when(recursoRepository.save(recurso)).thenReturn(recurso);

        Recurso creado = recursoService.crear(recurso);

        assertThat(creado.getIdProyectos()).containsExactly(10L, 20L);
        assertThat(creado.getIdProyecto()).isEqualTo(10L);
        verify(recursoRepository).save(recurso);
    }

    @Test
    void crearAceptaElFormatoAntiguoDeUnSoloProyecto() {
        Recurso recurso = recurso("Ana");
        recurso.setIdProyecto(15L);
        when(recursoRepository.save(recurso)).thenReturn(recurso);

        Recurso creado = recursoService.crear(recurso);

        assertThat(creado.getIdProyectos()).containsExactly(15L);
        verify(recursoRepository).save(recurso);
    }

    @Test
    void actualizarCopiaDatosPersonalesYProyectos() {
        Recurso existente = recurso("Ana");
        Recurso cambios = recurso("Valentina");
        cambios.setApellido("Soto");
        cambios.setEmail("valentina@innovatech.cl");
        cambios.setCargo("Lider QA");
        cambios.setDepartamento("Calidad");
        cambios.setDisponibilidad("OCUPADO");
        cambios.setNivelExperiencia("SENIOR");
        cambios.setIdProyectos(List.of(20L, 30L));

        when(recursoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(recursoRepository.save(existente)).thenReturn(existente);

        Recurso actualizado = recursoService.actualizar(1L, cambios);

        assertThat(actualizado.getNombre()).isEqualTo("Valentina");
        assertThat(actualizado.getApellido()).isEqualTo("Soto");
        assertThat(actualizado.getEmail()).isEqualTo("valentina@innovatech.cl");
        assertThat(actualizado.getCargo()).isEqualTo("Lider QA");
        assertThat(actualizado.getDepartamento()).isEqualTo("Calidad");
        assertThat(actualizado.getDisponibilidad()).isEqualTo("OCUPADO");
        assertThat(actualizado.getNivelExperiencia()).isEqualTo("SENIOR");
        assertThat(actualizado.getIdProyectos()).containsExactly(20L, 30L);
        verify(recursoRepository).save(existente);
    }

    @Test
    void actualizarLanzaExcepcionCuandoElRecursoNoExiste() {
        when(recursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recursoService.actualizar(99L, recurso("Inexistente")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Recurso no encontrado: 99");

        verify(recursoRepository).findById(99L);
    }

    @Test
    void eliminarDelegaLaOperacionAlRepository() {
        recursoService.eliminar(1L);

        verify(recursoRepository).deleteById(1L);
    }

    @Test
    void obtenerPorDisponibilidadDevuelveLosRecursosSolicitados() {
        List<Recurso> disponibles = List.of(recurso("Ana"));
        when(recursoRepository.findByDisponibilidad("DISPONIBLE")).thenReturn(disponibles);

        List<Recurso> resultado = recursoService.obtenerPorDisponibilidad("DISPONIBLE");

        assertThat(resultado).containsExactlyElementsOf(disponibles);
        verify(recursoRepository).findByDisponibilidad("DISPONIBLE");
    }

    @Test
    void obtenerPorProyectoConsideraRecursosConMultiplesProyectos() {
        Recurso asignado = recurso("Ana");
        asignado.setIdProyectos(List.of(10L, 20L));
        Recurso otroProyecto = recurso("Luis");
        otroProyecto.setIdProyectos(List.of(30L));
        Recurso sinProyecto = recurso("Sofia");
        sinProyecto.setIdProyectos(List.of());
        when(recursoRepository.findAll()).thenReturn(List.of(asignado, otroProyecto, sinProyecto));

        List<Recurso> resultado = recursoService.obtenerPorProyecto(20L);

        assertThat(resultado).containsExactly(asignado);
        verify(recursoRepository).findAll();
    }

    @Test
    void asignarProyectoReemplazaLaAsignacionSinCambiarDatosPersonales() {
        Recurso existente = recurso("Ana");
        existente.setIdProyectos(List.of(10L, 20L));
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(recursoRepository.save(existente)).thenReturn(existente);

        Recurso actualizado = recursoService.asignarProyecto(1L, 30L);

        assertThat(actualizado.getNombre()).isEqualTo("Ana");
        assertThat(actualizado.getIdProyectos()).containsExactly(30L);
        assertThat(actualizado.getIdProyecto()).isEqualTo(30L);
        verify(recursoRepository).save(existente);
    }

    @Test
    void asignarProyectoConNullQuitaLaAsignacion() {
        Recurso existente = recurso("Ana");
        existente.setIdProyectos(List.of(10L));
        when(recursoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(recursoRepository.save(existente)).thenReturn(existente);

        Recurso actualizado = recursoService.asignarProyecto(1L, null);

        assertThat(actualizado.getIdProyectos()).isEmpty();
        assertThat(actualizado.getIdProyecto()).isNull();
        verify(recursoRepository).save(existente);
    }

    @Test
    void asignarProyectoLanzaExcepcionCuandoElRecursoNoExiste() {
        when(recursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recursoService.asignarProyecto(99L, 10L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Recurso no encontrado: 99");

        verify(recursoRepository).findById(99L);
    }

    private Recurso recurso(String nombre) {
        Recurso recurso = new Recurso();
        recurso.setNombre(nombre);
        recurso.setApellido("Perez");
        recurso.setEmail(nombre.toLowerCase() + "@innovatech.cl");
        recurso.setCargo("Desarrollador Backend");
        recurso.setDepartamento("Desarrollo");
        recurso.setDisponibilidad("DISPONIBLE");
        recurso.setNivelExperiencia("SEMI_SENIOR");
        return recurso;
    }
}
