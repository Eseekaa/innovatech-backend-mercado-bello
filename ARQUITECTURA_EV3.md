# Arquitectura EV3 - Microservicio de Tareas y KPIs

Este documento define el plan tecnico para la Evaluacion Parcial 3. La idea es avanzar por hitos pequenos, con commits separados, para que el historial de GitHub muestre un proceso real de desarrollo.

## 1. Objetivo de la EV3

La nueva etapa del proyecto agrega gestion de tareas dentro de los proyectos. Cada proyecto podra tener muchas tareas, cada tarea tendra responsables, estado de avance y datos para reportes KPI.

Tambien se debe demostrar:

- Arquitectura de microservicios.
- Comunicacion entre frontend, BFF y backend mediante API REST.
- Persistencia de datos.
- Pruebas unitarias.
- Diagramas de arquitectura.
- Versionamiento en GitHub con avances progresivos.

## 2. Arquitectura propuesta

```text
Frontend React
     |
     v
BFF - Backend For Frontend :8084
     |
     +--> ms-proyectos :8081
     +--> ms-recursos  :8082
     +--> ms-tareas    :8086  NUEVO
     +--> ms-auth      :8083

API Gateway :8085
     |
     +--> /api/auth      -> ms-auth
     +--> /api/proyectos -> ms-proyectos
     +--> /api/recursos  -> ms-recursos
     +--> /api/tareas    -> ms-tareas
     +--> /api/bff       -> BFF
```

### Explicacion simple

El frontend no deberia hablar directamente con todos los microservicios para armar pantallas complejas. Para eso usamos el BFF, que junta informacion de proyectos, recursos y tareas en respuestas listas para la interfaz.

El API Gateway funciona como puerta de entrada tecnica. Permite tener un solo punto de acceso para las APIs y enrutar las peticiones al microservicio correcto.

## 3. Nuevo microservicio: ms-tareas

El microservicio `ms-tareas` sera responsable solo de la logica de tareas. Esto mantiene separada la responsabilidad:

- `ms-proyectos`: proyectos.
- `ms-recursos`: empleados/recursos humanos.
- `ms-tareas`: tareas, responsables y avance.
- `ms-auth`: usuarios, roles y JWT.
- `bff`: combinacion de datos para el frontend.

## 4. Modelo de datos de Tarea

```text
TAREA
-----
id
proyectoId
titulo
descripcion
estado
avance
prioridad
fechaInicio
fechaFin
responsableIds
```

### Campos principales

- `proyectoId`: identifica a que proyecto pertenece la tarea.
- `titulo`: nombre corto de la tarea.
- `descripcion`: detalle de la tarea.
- `estado`: situacion actual de la tarea.
- `avance`: porcentaje entre 0 y 100.
- `prioridad`: importancia de la tarea.
- `fechaInicio`: fecha estimada de inicio.
- `fechaFin`: fecha estimada de termino.
- `responsableIds`: empleados asignados a la tarea.

## 5. Estados de tarea

```text
PENDIENTE
EN_PROGRESO
COMPLETADA
BLOQUEADA
```

### Reglas propuestas

- Una tarea nueva parte normalmente como `PENDIENTE`.
- Si el avance es mayor que 0 y menor que 100, puede quedar `EN_PROGRESO`.
- Si el avance llega a 100, la tarea queda `COMPLETADA`.
- Si existe un problema que impide avanzar, queda `BLOQUEADA`.

Estas reglas ayudan a que los KPI sean coherentes.

## 6. Relacion entre proyectos, tareas y recursos

```text
PROYECTO 1 ---- N TAREAS
TAREA    N ---- N RECURSOS
```

### Explicacion para defensa

Un proyecto puede tener muchas tareas. Una tarea pertenece a un solo proyecto. Una tarea puede tener uno o varios responsables, y un recurso tambien puede estar asignado a varias tareas.

Como son microservicios separados, `ms-tareas` no usa llaves foraneas reales contra las bases de datos de `ms-proyectos` o `ms-recursos`. En su lugar guarda los IDs:

- `proyectoId` apunta al ID del proyecto.
- `responsableIds` apunta a los IDs de recursos.

Esto es normal en microservicios porque cada servicio es dueno de su propia base de datos.

## 7. Endpoints REST de ms-tareas

```text
GET    /api/tareas
GET    /api/tareas/{id}
GET    /api/tareas/proyecto/{proyectoId}
GET    /api/tareas/responsable/{responsableId}
POST   /api/tareas
PUT    /api/tareas/{id}
PATCH  /api/tareas/{id}/estado
DELETE /api/tareas/{id}
```

### Para que sirve cada endpoint

- `GET /api/tareas`: lista todas las tareas.
- `GET /api/tareas/{id}`: busca una tarea por ID.
- `GET /api/tareas/proyecto/{proyectoId}`: lista tareas de un proyecto.
- `GET /api/tareas/responsable/{responsableId}`: lista tareas asignadas a un empleado.
- `POST /api/tareas`: crea una tarea.
- `PUT /api/tareas/{id}`: edita una tarea completa.
- `PATCH /api/tareas/{id}/estado`: actualiza solo estado y avance.
- `DELETE /api/tareas/{id}`: elimina una tarea.

## 8. Integracion con BFF

El BFF agregara endpoints equivalentes para que el frontend consuma una API mas comoda:

```text
GET    /api/bff/tareas
GET    /api/bff/tareas/{id}
GET    /api/bff/tareas/proyecto/{proyectoId}
GET    /api/bff/tareas/responsable/{responsableId}
POST   /api/bff/tareas
PUT    /api/bff/tareas/{id}
PATCH  /api/bff/tareas/{id}/estado
DELETE /api/bff/tareas/{id}
GET    /api/bff/dashboard
```

El dashboard se ampliara para incluir indicadores de tareas y KPIs.

## 9. KPIs propuestos

### KPIs generales

```text
totalTareas
tareasPendientes
tareasEnProgreso
tareasCompletadas
tareasBloqueadas
avancePromedioTareas
```

### KPIs por proyecto

```text
proyectoId
nombreProyecto
totalTareas
tareasCompletadas
tareasBloqueadas
avancePromedio
porcentajeCumplimiento
```

### KPIs por recurso

```text
recursoId
nombreRecurso
totalTareasAsignadas
tareasCompletadas
tareasPendientes
avancePromedio
```

## 10. Roles y permisos

```text
USUARIO
JEFE_PROYECTO
ADMIN
```

### USUARIO

Puede:

- Ver dashboard.
- Ver proyectos y recursos.
- Ver sus tareas asignadas.
- Cambiar estado y avance de sus propias tareas.

### JEFE_PROYECTO

Puede:

- Hacer todo lo del USUARIO.
- Crear tareas.
- Editar tareas.
- Asignar responsables.
- Revisar KPIs del equipo y de los proyectos.

### ADMIN

Puede:

- Hacer todo lo del JEFE_PROYECTO.
- Eliminar tareas.
- Administrar proyectos y recursos completos.

## 11. Pruebas unitarias planificadas

Para `ms-tareas` se probaran:

- Crear tarea correctamente.
- Validar que una tarea tenga proyecto.
- Validar que el avance este entre 0 y 100.
- Actualizar estado y avance.
- Listar tareas por proyecto.
- Listar tareas por responsable.
- Calcular KPIs basicos.

Para el BFF se probaran:

- Que consulte tareas desde `ms-tareas`.
- Que combine datos de proyectos, recursos y tareas.
- Que el dashboard incluya los nuevos KPIs.

## 12. Plan de commits

Se trabajara en commits pequenos para mostrar avance real:

```text
docs: definir arquitectura ev3 de tareas y kpis
feat: crear microservicio ms-tareas base
feat: implementar crud de tareas
feat: agregar asignacion de responsables a tareas
feat: integrar ms-tareas con bff y gateway
feat: agregar kpis de tareas al dashboard
test: agregar pruebas unitarias para ms-tareas
docs: actualizar readme con arquitectura ev3
```

## 13. Primer hito tecnico

El primer hito de codigo sera crear `ms-tareas` con:

- Spring Boot.
- Java 17.
- Maven.
- H2 Database.
- Spring Data JPA.
- Spring Web.
- Validaciones.
- Dockerfile.

Todavia no se modificara el frontend hasta que el backend tenga endpoints estables.

