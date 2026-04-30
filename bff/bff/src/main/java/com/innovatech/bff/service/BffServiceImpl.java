package com.innovatech.bff.service;

import com.innovatech.bff.dto.DashboardDTO;
import com.innovatech.bff.dto.ProyectoDTO;
import com.innovatech.bff.dto.RecursoDTO;
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

    // URLs de los microservicios
    private static final String MS_PROYECTOS = "http://localhost:8081/api/proyectos";
    private static final String MS_RECURSOS = "http://localhost:8082/api/recursos";

    // RestTemplate es el cliente HTTP de Spring para llamar a otros servicios
    private final RestTemplate restTemplate;

    public BffServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
                MS_PROYECTOS, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ProyectoDTO>>() {});
        return response.getBody();
    }

    @Override
    public ProyectoDTO crearProyecto(ProyectoDTO proyectoDTO) {
        return restTemplate.postForObject(MS_PROYECTOS, proyectoDTO, ProyectoDTO.class);
    }

    @Override
    public ProyectoDTO actualizarProyecto(Long id, ProyectoDTO proyectoDTO) {
        restTemplate.put(MS_PROYECTOS + "/" + id, proyectoDTO);
        return restTemplate.getForObject(MS_PROYECTOS + "/" + id, ProyectoDTO.class);
    }

    @Override
    public void eliminarProyecto(Long id) {
        restTemplate.delete(MS_PROYECTOS + "/" + id);
    }

    @Override
    public List<RecursoDTO> obtenerRecursos() {
        ResponseEntity<List<RecursoDTO>> response = restTemplate.exchange(
                MS_RECURSOS, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<RecursoDTO>>() {});
        return response.getBody();
    }

    @Override
    public RecursoDTO crearRecurso(RecursoDTO recursoDTO) {
        return restTemplate.postForObject(MS_RECURSOS, recursoDTO, RecursoDTO.class);
    }

    @Override
    public RecursoDTO actualizarRecurso(Long id, RecursoDTO recursoDTO) {
        restTemplate.put(MS_RECURSOS + "/" + id, recursoDTO);
        return restTemplate.getForObject(MS_RECURSOS + "/" + id, RecursoDTO.class);
    }

    @Override
    public void eliminarRecurso(Long id) {
        restTemplate.delete(MS_RECURSOS + "/" + id);
    }
}