# Innovatech Solutions - Backend

Backend de la Plataforma Inteligente de Gestion de Proyectos y Recursos Humanos para Innovatech Solutions.

El sistema esta construido con arquitectura de microservicios en Spring Boot. Incluye autenticacion con JWT, gestion de proyectos, gestion de recursos humanos, gestion de tareas, BFF para el frontend, API Gateway, pruebas unitarias, cobertura JaCoCo y soporte para Docker.

## Integrantes

- Matias Mercado
- Matias Bello

## Arquitectura

```text
Frontend React
    |
    v
API Gateway :8085
    |
    +--> MS Auth      :8083
    +--> MS Proyectos :8081
    +--> MS Recursos  :8082
    +--> MS Tareas    :8086
    +--> BFF          :8084
              |
              +--> MS Proyectos
              +--> MS Recursos
              +--> MS Tareas
```

## Microservicios

| Servicio | Puerto | Responsabilidad |
| --- | --- | --- |
| api-gateway | 8085 | Puerta de entrada para enrutar peticiones a los servicios |
| bff | 8084 | Une informacion de proyectos, recursos y tareas para el frontend |
| ms-auth | 8083 | Registro, login, roles, BCrypt y JWT |
| ms-proyectos | 8081 | CRUD de proyectos y visto bueno |
| ms-recursos | 8082 | CRUD de empleados y asignacion a proyectos |
| ms-tareas | 8086 | CRUD de tareas, asignacion de responsables, avance, visto bueno y KPIs |

## Funcionalidades principales

- Registro e inicio de sesion con token JWT.
- Token con duracion maxima de 10 minutos.
- Contrasenas encriptadas con BCrypt.
- Validacion de usuarios y contrasenas.
- Roles jerarquicos: `USUARIO`, `JEFE_PROYECTO`, `ADMIN`.
- CRUD de proyectos.
- CRUD de recursos humanos.
- Asignacion de uno o mas proyectos a cada recurso.
- CRUD de tareas por proyecto.
- Asignacion de uno o mas responsables a cada tarea.
- Control de estado, prioridad, avance y visto bueno de cierre en tareas.
- KPIs de tareas por estado, proyecto y responsable.
- Visto bueno de proyectos, con opcion para aprobar o quitar aprobacion.
- Dashboard combinado desde el BFF.
- API Gateway para exponer los servicios de forma centralizada.
- Bases de datos H2 en memoria para facilitar la presentacion.

## Roles

| Rol | Permisos |
| --- | --- |
| USUARIO | Visualiza informacion y puede consultar proyectos, recursos y tareas |
| JEFE_PROYECTO | Hereda permisos de USUARIO, crea/edita proyectos, recursos y tareas, asigna responsables y gestiona avance |
| ADMIN | Hereda permisos anteriores y ademas puede eliminar proyectos, recursos y tareas |

Los roles especiales se pueden asignar desde la base de datos H2 de `ms-auth`.

## Tecnologias

- Java 17
- Spring Boot 3.5.14
- Spring Security
- JWT
- BCrypt
- Spring Data JPA
- H2 Database
- Maven
- JaCoCo
- Docker y Docker Compose

## Ejecucion recomendada con Docker

Antes de ejecutar, Docker Desktop debe estar abierto.

Los repositorios deben quedar en la misma carpeta padre:

```text
innovatech
|-- innovatech-backend-mercado-bello
`-- innovatech-frontend-mercado-bello
    `-- innovatech-frontend
```

Comando:

```powershell
cd C:\Users\%USERNAME%\innovatech\innovatech-backend-mercado-bello
docker compose up -d --build
```

Luego abrir:

```text
http://localhost:3000
```

Para verificar los contenedores:

```powershell
docker compose ps
```

Para detener:

```powershell
docker compose down
```

## Ejecucion manual sin Docker

Abrir una terminal por cada servicio.

```powershell
cd ms-auth\ms-auth
.\mvnw.cmd spring-boot:run
```

```powershell
cd ms-proyectos\ms-proyectos
.\mvnw.cmd spring-boot:run
```

```powershell
cd ms-recursos\ms-recursos
.\mvnw.cmd spring-boot:run
```

```powershell
cd ms-tareas
..\api-gateway\mvnw.cmd spring-boot:run
```

```powershell
cd bff\bff
.\mvnw.cmd spring-boot:run
```

```powershell
cd api-gateway
.\mvnw.cmd spring-boot:run
```

## Ejecucion de Pruebas Unitarias y Cobertura (JaCoCo)

Para compilar, ejecutar las pruebas unitarias y verificar el reporte de cobertura minimo del 60% exigido por la pauta, ubicate en la carpeta correspondiente de cada modulo y ejecuta el siguiente comando:

```powershell
# API Gateway
cd api-gateway
.\mvnw.cmd clean verify

# BFF
cd bff\bff
.\mvnw.cmd clean verify

# MS Auth
cd ms-auth\ms-auth
.\mvnw.cmd clean verify

# MS Proyectos
cd ms-proyectos\ms-proyectos
.\mvnw.cmd clean verify

# MS Recursos
cd ms-recursos\ms-recursos
.\mvnw.cmd clean verify

# MS Tareas
cd ms-tareas
..\api-gateway\mvnw.cmd clean verify
```

### Ubicacion de los Reportes de Cobertura JaCoCo

Una vez ejecutado el comando `verify`, los reportes interactivos en HTML se generan en las siguientes rutas relativas:

- **API Gateway:** `api-gateway\target\site\jacoco\index.html`
- **BFF (Backend For Frontend):** `bff\bff\target\site\jacoco\index.html`
- **MS Auth:** `ms-auth\ms-auth\target\site\jacoco\index.html`
- **MS Proyectos:** `ms-proyectos\ms-proyectos\target\site\jacoco\index.html`
- **MS Recursos:** `ms-recursos\ms-recursos\target\site\jacoco\index.html`
- **MS Tareas:** `ms-tareas\target\site\jacoco\index.html`

## Cobertura validada

Los componentes backend superan el minimo de 60% de cobertura solicitado.

| Componente | Cobertura aproximada |
| --- | --- |
| API Gateway | 100% |
| BFF | 86% |
| MS Auth | 84% |
| MS Proyectos | 72% |
| MS Recursos | 72% |
| MS Tareas | 69% |

El detalle de ejecucion y resultados se encuentra en `INFORME_EV3_COBERTURA.md`.

## Endpoints principales

### Auth

| Metodo | URL | Descripcion |
| --- | --- | --- |
| POST | `/api/auth/register` | Registrar usuario |
| POST | `/api/auth/login` | Iniciar sesion |

### BFF

| Metodo | URL | Descripcion |
| --- | --- | --- |
| GET | `/api/bff/dashboard` | Resumen combinado |
| GET | `/api/bff/proyectos` | Listar proyectos con empleados asignados |
| POST | `/api/bff/proyectos` | Crear proyecto |
| PUT | `/api/bff/proyectos/{id}` | Actualizar proyecto o visto bueno |
| DELETE | `/api/bff/proyectos/{id}` | Eliminar proyecto |
| GET | `/api/bff/recursos` | Listar recursos |
| POST | `/api/bff/recursos` | Crear recurso |
| PUT | `/api/bff/recursos/{id}` | Actualizar recurso y proyectos asignados |
| DELETE | `/api/bff/recursos/{id}` | Eliminar recurso |
| GET | `/api/bff/tareas` | Listar tareas |
| GET | `/api/bff/tareas/{id}` | Obtener tarea por id |
| GET | `/api/bff/tareas/proyecto/{proyectoId}` | Listar tareas por proyecto |
| GET | `/api/bff/tareas/responsable/{responsableId}` | Listar tareas por responsable |
| POST | `/api/bff/tareas` | Crear tarea |
| PUT | `/api/bff/tareas/{id}` | Actualizar tarea |
| PATCH | `/api/bff/tareas/{id}/estado` | Cambiar estado y avance |
| DELETE | `/api/bff/tareas/{id}` | Eliminar tarea |
| GET | `/api/bff/tareas/kpis` | KPIs generales de tareas |
| GET | `/api/bff/tareas/kpis/proyectos` | KPIs de tareas por proyecto |
| GET | `/api/bff/tareas/kpis/responsables` | KPIs de tareas por responsable |

## Consolas H2

| Servicio | URL | JDBC URL |
| --- | --- | --- |
| Auth | `http://localhost:8083/h2-console` | `jdbc:h2:mem:authdb` |
| Proyectos | `http://localhost:8081/h2-console` | `jdbc:h2:mem:proyectosdb` |
| Recursos | `http://localhost:8082/h2-console` | `jdbc:h2:mem:recursosdb` |
| Tareas | `http://localhost:8086/h2-console` | `jdbc:h2:mem:tareasdb` |

Credenciales:

```text
User Name: sa
Password: vacio
```

Consultas utiles:

```sql
SELECT ID, USERNAME, EMAIL, ROL, PASSWORD FROM USUARIOS;
SELECT * FROM PROYECTOS;
SELECT * FROM RECURSOS;
SELECT * FROM RECURSO_PROYECTOS;
SELECT * FROM TAREAS;
```

Asignar roles:

```sql
UPDATE USUARIOS SET ROL = 'ADMIN' WHERE USERNAME = 'admin1';
UPDATE USUARIOS SET ROL = 'JEFE_PROYECTO' WHERE USERNAME = 'jefe1';
UPDATE USUARIOS SET ROL = 'USUARIO' WHERE USERNAME = 'usuario1';
```

## Nota sobre H2

Las bases de datos H2 estan en memoria. Si se apagan los servicios o se ejecuta `docker compose down`, los datos creados durante la prueba se borran. El codigo del proyecto no se borra.

## Documentacion EV3

- `ARQUITECTURA_EV3.md`: arquitectura de microservicios para tareas y KPIs.
- `INFORME_EV3_COBERTURA.md`: resumen de pruebas unitarias, cobertura JaCoCo y resultados.
- `DOCKER.md`: guia de ejecucion con Docker.
- `GUIA_PRESENTACION.md`: apoyo para la defensa del proyecto.

## Branching

Se uso GitHub Flow:

- `main`: version estable.
- `feature/backend-matias-mercado`: desarrollo inicial del backend.
- `feature/frontend-matias-bello`: desarrollo inicial del frontend.
- `feature/backend-ev3-tareas-kpi`: desarrollo EV3 de tareas, KPIs, integracion y cobertura.
- `feature/backend-ev3-testing-matias-bello`: pruebas, JaCoCo y documentacion de cobertura.
