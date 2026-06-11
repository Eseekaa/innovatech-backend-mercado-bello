package com.innovatech.bff.service;

import com.innovatech.bff.dto.DashboardDTO;
import com.innovatech.bff.dto.ProyectoDTO;
import com.innovatech.bff.dto.RecursoDTO;
import com.innovatech.bff.dto.TareaDTO;
import com.innovatech.bff.dto.ActualizarEstadoTareaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.List;

// Implementación del BFF - llama a los microservicios via HTTP
// Patrón Dependency Injection: Spring inyecta el RestTemplate
@Service
public class BffServiceImpl implements BffService {

    // URLs de los microservicios.
    // En ejecucion normal vienen desde application.properties con localhost.
    // En Docker se sobrescriben desde docker-compose.yml usando nombres de contenedor.
    private final String msProyectosUrl;
    private final String msRecursosUrl;
    private final String msTareasUrl;

    // RestTemplate es el cliente HTTP de Spring para llamar a otros servicios
    private final RestTemplate restTemplate;

    public BffServiceImpl(
            RestTemplate restTemplate,
            @Value("${ms.proyectos.url}") String msProyectosUrl,
            @Value("${ms.recursos.url}") String msRecursosUrl,
            @Value("${ms.tareas.url}") String msTareasUrl) {
        this.restTemplate = restTemplate;
        this.msProyectosUrl = msProyectosUrl;
        this.msRecursosUrl = msRecursosUrl;
        this.msTareasUrl = msTareasUrl;
    }

    // Combina datos de ambos microservicios en un solo dashboard
    @Override
    public DashboardDTO obtenerDashboard() {
        List<ProyectoDTO> proyectos = obtenerProyectos();
        List<RecursoDTO> recursos = obtenerRecursos();

        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setProyectos(proyectos);
        dashboard.setRecursos(recursos);
        dashboard.setTotalProyectos(proyectos.size());
        dashboard.setTotalRecursos(recursos.size());

        // Cuenta recursos disponibles filtrando por disponibilidad
        long disponibles = recursos.stream()
                .filter(r -> "DISPONIBLE".equals(r.getDisponibilidad()))
                .count();
        dashboard.setRecursosDisponibles((int) disponibles);

        // Cuenta proyectos activos filtrando por estado
        long activos = proyectos.stream()
                .filter(p -> "ACTIVO".equals(p.getEstado()))
                .count();
        dashboard.setProyectosActivos((int) activos);

        return dashboard;
    }

    @Override
    public List<ProyectoDTO> obtenerProyectos() {
        ResponseEntity<List<ProyectoDTO>> response = restTemplate.exchange(
                msProyectosUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ProyectoDTO>>() {});
        return response.getBody();
    }

    @Override
    public ProyectoDTO crearProyecto(ProyectoDTO proyectoDTO) {
        // Todo proyecto nuevo parte pendiente de visto bueno.
        // Evita enviar null al microservicio de proyectos.
        if (proyectoDTO.getVistoBueno() == null) {
            proyectoDTO.setVistoBueno(false);
        }
        return restTemplate.postForObject(msProyectosUrl, proyectoDTO, ProyectoDTO.class);
    }

    @Override
    public ProyectoDTO actualizarProyecto(Long id, ProyectoDTO proyectoDTO) {
        restTemplate.put(msProyectosUrl + "/" + id, proyectoDTO);
        return restTemplate.getForObject(msProyectosUrl + "/" + id, ProyectoDTO.class);
    }

    @Override
    public void eliminarProyecto(Long id) {
        restTemplate.delete(msProyectosUrl + "/" + id);
    }

    @Override
    public List<RecursoDTO> obtenerRecursos() {
        ResponseEntity<List<RecursoDTO>> response = restTemplate.exchange(
                msRecursosUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<RecursoDTO>>() {});
        return response.getBody();
    }

    @Override
    public RecursoDTO crearRecurso(RecursoDTO recursoDTO) {
        return restTemplate.postForObject(msRecursosUrl, recursoDTO, RecursoDTO.class);
    }

    @Override
    public RecursoDTO actualizarRecurso(Long id, RecursoDTO recursoDTO) {
        restTemplate.put(msRecursosUrl + "/" + id, recursoDTO);
        return restTemplate.getForObject(msRecursosUrl + "/" + id, RecursoDTO.class);
    }

    @Override
    public void eliminarRecurso(Long id) {
        restTemplate.delete(msRecursosUrl + "/" + id);
    }

    // Obtiene todos los recursos asignados a un proyecto específico
    // Llama al ms-recursos filtrando por idProyecto
    @Override
    public RecursoDTO asignarProyectoARecurso(Long id, Long idProyecto) {
        // RestTemplate por defecto puede fallar con PATCH en Windows/JDK.
        // Por eso el BFF obtiene el empleado, cambia solo idProyecto y lo guarda con PUT.
        RecursoDTO recurso = restTemplate.getForObject(msRecursosUrl + "/" + id, RecursoDTO.class);
        if (recurso == null) {
            throw new RuntimeException("Recurso no encontrado: " + id);
        }
        recurso.setIdProyecto(idProyecto);
        restTemplate.put(msRecursosUrl + "/" + id, recurso);
        return restTemplate.getForObject(msRecursosUrl + "/" + id, RecursoDTO.class);
    }

    @Override
    public List<RecursoDTO> obtenerRecursosPorProyecto(Long idProyecto) {
        ResponseEntity<List<RecursoDTO>> response = restTemplate.exchange(
                msRecursosUrl + "/proyecto/" + idProyecto,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<RecursoDTO>>() {});
        return response.getBody();
    }

    @Override
    public List<TareaDTO> obtenerTareas() {
        ResponseEntity<List<TareaDTO>> response = restTemplate.exchange(
                msTareasUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TareaDTO>>() {});
        return response.getBody();
    }

    @Override
    public TareaDTO obtenerTareaPorId(Long id) {
        return restTemplate.getForObject(msTareasUrl + "/" + id, TareaDTO.class);
    }

    @Override
    public List<TareaDTO> obtenerTareasPorProyecto(Long proyectoId) {
        ResponseEntity<List<TareaDTO>> response = restTemplate.exchange(
                msTareasUrl + "/proyecto/" + proyectoId,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TareaDTO>>() {});
        return response.getBody();
    }

    @Override
    public List<TareaDTO> obtenerTareasPorResponsable(Long responsableId) {
        ResponseEntity<List<TareaDTO>> response = restTemplate.exchange(
                msTareasUrl + "/responsable/" + responsableId,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TareaDTO>>() {});
        return response.getBody();
    }

    @Override
    public TareaDTO crearTarea(TareaDTO tareaDTO) {
        // Valores por defecto para que el frontend pueda crear tareas simples.
        if (tareaDTO.getEstado() == null) {
            tareaDTO.setEstado("PENDIENTE");
        }
        if (tareaDTO.getAvance() == null) {
            tareaDTO.setAvance(0);
        }
        if (tareaDTO.getPrioridad() == null) {
            tareaDTO.setPrioridad("MEDIA");
        }
        return restTemplate.postForObject(msTareasUrl, tareaDTO, TareaDTO.class);
    }

    @Override
    public TareaDTO actualizarTarea(Long id, TareaDTO tareaDTO) {
        restTemplate.put(msTareasUrl + "/" + id, tareaDTO);
        return restTemplate.getForObject(msTareasUrl + "/" + id, TareaDTO.class);
    }

    @Override
    public TareaDTO actualizarEstadoTarea(Long id, ActualizarEstadoTareaDTO dto) {
        // El frontend usa PATCH contra el BFF, pero internamente usamos GET + PUT.
        // Esto evita problemas del RestTemplate con PATCH en algunos entornos Windows/JDK.
        TareaDTO tarea = restTemplate.getForObject(msTareasUrl + "/" + id, TareaDTO.class);
        if (tarea == null) {
            throw new RuntimeException("Tarea no encontrada: " + id);
        }
        tarea.setEstado(dto.getEstado());
        tarea.setAvance(dto.getAvance());
        restTemplate.put(msTareasUrl + "/" + id, tarea);
        return restTemplate.getForObject(msTareasUrl + "/" + id, TareaDTO.class);
    }

    @Override
    public void eliminarTarea(Long id) {
        restTemplate.delete(msTareasUrl + "/" + id);
    }
}
