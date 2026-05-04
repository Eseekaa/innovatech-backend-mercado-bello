# 🏢 Innovatech Solutions — Backend

Backend completo de la Plataforma Inteligente de Gestión de Proyectos y Recursos Humanos.

## 👥 Integrantes
- Matías Mercado
- Matías Bello

## 📋 Descripción
Sistema backend basado en arquitectura de microservicios para la empresa Innovatech Solutions. Permite gestionar proyectos tecnológicos y recursos humanos con autenticación segura mediante JWT.

## 🏗️ Arquitectura

Frontend React
↓
BFF (8084)
↓       ↓
MS-Proyectos  MS-Recursos
(8081)      (8082)
MS-Auth (8083)

## 📦 Componentes

### 🔐 ms-auth (puerto 8083)
Microservicio de autenticación. Gestiona registro e inicio de sesión con tokens JWT y encriptación BCrypt.

### 📋 ms-proyectos (puerto 8081)
Microservicio de gestión de proyectos. CRUD completo con estados: ACTIVO, EN_PAUSA, COMPLETADO, CANCELADO.

### 👥 ms-recursos (puerto 8082)
Microservicio de gestión de recursos humanos. CRUD completo de empleados con disponibilidad y nivel de experiencia.

### 🔀 BFF - Backend For Frontend (puerto 8084)
Intermediario entre el frontend y los microservicios. Agrega datos de múltiples servicios en una sola respuesta.

## 🎨 Patrones de Diseño Aplicados
- **Repository Pattern** — Separación de la lógica de acceso a datos
- **Strategy Pattern** — Interfaces de servicio con implementaciones intercambiables
- **Dependency Injection** — Spring inyecta dependencias automáticamente
- **DTO Pattern** — Objetos de transferencia de datos entre capas
- **BFF Pattern** — Backend específico para el frontend
- **Factory Method** — Spring como fábrica de objetos (beans)

## 🛠️ Tecnologías
- Java 17+
- Spring Boot 3.5.14
- Spring Security + JWT
- Spring Data JPA
- H2 Database (en memoria)
- Maven

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java 17 o superior
- Maven 3.9+

### Levantar cada microservicio

**ms-auth:**
```bash
cd ms-auth/ms-auth
mvn spring-boot:run
```

**ms-proyectos:**
```bash
cd ms-proyectos/ms-proyectos
mvn spring-boot:run
```

**ms-recursos:**
```bash
cd ms-recursos/ms-recursos
mvn spring-boot:run
```

**BFF:**
```bash
cd bff/bff
mvn spring-boot:run
```

## 🌐 Endpoints Principales

### Autenticación
| Método | URL | Descripción |
|--------|-----|-------------|
| POST | /api/auth/register | Registrar usuario |
| POST | /api/auth/login | Iniciar sesión |

### BFF — Proyectos
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/bff/proyectos | Listar proyectos |
| POST | /api/bff/proyectos | Crear proyecto |
| PUT | /api/bff/proyectos/{id} | Actualizar proyecto |
| DELETE | /api/bff/proyectos/{id} | Eliminar proyecto |

### BFF — Recursos
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/bff/recursos | Listar empleados |
| POST | /api/bff/recursos | Crear empleado |
| PUT | /api/bff/recursos/{id} | Actualizar empleado |
| DELETE | /api/bff/recursos/{id} | Eliminar empleado |

### BFF — Dashboard
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/bff/dashboard | Estadísticas combinadas |

## 🗄️ Consolas H2 (Base de Datos)
- Auth: http://localhost:8083/h2-console (JDBC: jdbc:h2:mem:authdb)
- Proyectos: http://localhost:8081/h2-console (JDBC: jdbc:h2:mem:proyectosdb)
- Recursos: http://localhost:8082/h2-console (JDBC: jdbc:h2:mem:recursosdb)

## 🌿 Estrategia de Branching
Se utilizó **GitHub Flow**:
- `main` — rama principal con código estable
- `feature/backend-matias-mercado` — desarrollo del backend
