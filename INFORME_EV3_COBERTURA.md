# Informe de Pruebas Unitarias y Cobertura de Código (EV3)
**Proyecto:** Innovatech Solutions - Backend  
**Integrantes:** Matías Mercado, Matías Bello  
**Fecha de Evaluación:** 15 de Junio, 2026  

---

## 1. Introducción
Este informe detalla las actividades de aseguramiento de calidad, ejecución de pruebas unitarias y análisis de cobertura de código para los componentes del backend de **Innovatech Solutions**. El backend está estructurado con una arquitectura de microservicios robusta y escalable que utiliza Spring Boot, Spring Cloud Gateway, un componente BFF (Backend For Frontend) y bases de datos relacionales en memoria H2.

En esta etapa (EV3), se ha incorporado la gestión de tareas y cálculo de KPIs a través de un nuevo microservicio (`ms-tareas`) y se ha implementado el análisis de cobertura de código utilizando el plugin **JaCoCo (Java Code Coverage)**.

---

## 2. Objetivos
* **Verificar el correcto arranque y funcionamiento** del contexto de todos los microservicios backend.
* **Asegurar la calidad del software** mediante la ejecución de un conjunto completo de pruebas unitarias automatizadas.
* **Garantizar una cobertura mínima del 60% de líneas de código (LINE)** en todos los componentes del backend, conforme a las exigencias técnicas de la evaluación.
* **Generar evidencias técnicas** auditables para la entrega y defensa del proyecto EV3.

---

## 3. Herramientas Utilizadas
* **Spring Boot 3.5.14** como framework principal de microservicios.
* **Java 17 y 21 (OpenJDK)** como plataformas de ejecución del lenguaje.
* **JUnit 5 (Jupyter)** como motor principal para la definición y ejecución de pruebas unitarias.
* **Mockito** para el mockeo y aislamiento de llamadas externas y dependencias en los controladores y servicios.
* **JaCoCo 0.8.15 (jacoco-maven-plugin)** para la medición de la cobertura de código en tiempo de compilación y verificación.
* **Maven Wrapper (mvnw)** para estandarizar el ciclo de vida del build sin depender de instalaciones globales de Maven.
* **H2 Database Engine** como base de datos SQL relacional en memoria para aislamiento de pruebas de persistencia.

---

## 4. Estrategia de Pruebas
La estrategia se centró en el desarrollo de pruebas a nivel de unidad e integración de componentes locales (Slice Testing):
* **Capa de Persistencia:** Validación de entidades JPA e interacciones directas contra la base de datos relacional H2.
* **Capa de Servicio:** Validación de reglas de negocio, lógica algorítmica y cálculo de KPIs. Las dependencias externas (como `RestTemplate` en el BFF) se aislaron utilizando Mockito.
* **Capa de Controladores (REST API):** Pruebas funcionales de endpoints para verificar códigos HTTP, serialización JSON y control de excepciones.
* **Capa de Seguridad (ms-auth):** Validación específica para encriptación de credenciales con BCrypt y ciclo de vida de JSON Web Tokens (JWT).

---

## 5. Casos de Prueba Ejecutados
A continuación se resumen los flujos principales evaluados en cada uno de los componentes:

* **API Gateway:**
  * Carga inicial y levantamiento correcto del contexto de enrutamiento Spring Cloud WebMVC.
  * Arranque de la clase principal `ApiGatewayApplication.main(...)` configurada dinámicamente en puerto aleatorio.
* **BFF (Backend For Frontend):**
  * Agregación y consolidación de datos desde los distintos microservicios para armar la respuesta unificada del Dashboard.
  * Gestión de proyectos, recursos y tareas intermediando solicitudes HTTP de forma aislada.
  * Validación del cálculo integrado de KPIs.
* **ms-auth (Servicio de Autenticación):**
  * Registro exitoso de usuarios con validaciones de campos únicos (email, username).
  * Inicio de sesión con autenticación exitosa, encriptación BCrypt y generación de token JWT firmado.
  * Validación de expiración de token y del filtro de seguridad `JwtFilter`.
* **ms-proyectos:**
  * Operaciones CRUD completas para proyectos y persistencia en H2.
  * Flujo de aprobación técnica ("Visto Bueno") de proyectos por parte de roles autorizados.
* **ms-recursos:**
  * Operaciones CRUD de empleados y verificación de su estado de disponibilidad (`DISPONIBLE`, `ASIGNADO`).
  * Asociación y desasociación de recursos a proyectos específicos.
  * Filtrado de recursos por proyecto.
* **ms-tareas:**
  * Creación y asignación de tareas a un `proyectoId` con estados iniciales válidos (`PENDIENTE`).
  * Transición de estados y porcentaje de avance (`avance` de 0% a 100%).
  * Cálculo de KPIs consolidados a nivel global, por proyecto y por responsable (avance promedio, total completadas, etc.).

---

## 6. Resultados Obtenidos
La ejecución en limpio (`clean verify`) arrojó una tasa de éxito perfecta, sin fallas ni errores detectados en el código de producción.

* **Total de pruebas backend ejecutadas:** 68
* **Pruebas exitosas:** 68
* **Pruebas fallidas:** 0
* **Errores de compilación o dependencias:** Ninguno

---

## 7. Cobertura por Componente
Todos los módulos superaron el umbral de calidad mínimo del **60%**, situándose la mayoría por encima del **70%** y logrando niveles excepcionales (más del 80%) en los componentes neurálgicos de negocio y seguridad.

| Módulo Backend | Pruebas Ejecutadas | Pruebas Exitosas | Porcentaje Cobertura (JaCoCo) | Estado de Calidad |
| :--- | :---: | :---: | :---: | :---: |
| **API Gateway** | 2 | 2 | **100.00%** | **APROBADO** |
| **BFF** | 22 | 22 | **85.06%** | **APROBADO** |
| **ms-auth** | 13 | 13 | **85.34%** | **APROBADO** |
| **ms-proyectos** | 10 | 10 | **72.88%** | **APROBADO** |
| **ms-recursos** | 13 | 13 | **71.91%** | **APROBADO** |
| **ms-tareas** | 8 | 8 | **69.14%** | **APROBADO** |

---

## 8. Evidencias Recopiladas y Reportes
Los reportes completos de cobertura están accesibles de forma local tras compilar el proyecto. A continuación se detallan las rutas físicas de cada reporte y las capturas recomendadas para el documento de entrega final:

### Ubicaciones físicas de los Reportes de Cobertura JaCoCo:
* **API Gateway:** `api-gateway\target\site\jacoco\index.html`
* **BFF:** `bff\bff\target\site\jacoco\index.html`
* **ms-auth:** `ms-auth\ms-auth\target\site\jacoco\index.html`
* **ms-proyectos:** `ms-proyectos\ms-proyectos\target\site\jacoco\index.html`
* **ms-recursos:** `ms-recursos\ms-recursos\target\site\jacoco\index.html`
* **ms-tareas:** `ms-tareas\target\site\jacoco\index.html`

### Capturas Sugeridas para el Informe:
1. **Captura 1 (API Gateway):** Vista del reporte JaCoCo `index.html` del Gateway mostrando 100% de cobertura en la clase principal.
2. **Captura 2 (BFF y ms-auth):** Reporte de cobertura del BFF y ms-auth demostrando la cobertura superior al 85% en sus controladores y servicios clave.
3. **Captura 3 (ms-proyectos y ms-recursos):** Reporte JaCoCo mostrando que la lógica de negocio básica está adecuadamente probada y por encima de los límites exigidos (71%-72%).
4. **Captura 4 (ms-tareas):** Reporte JaCoCo para el nuevo microservicio de tareas demostrando un 69.14% de cobertura, destacando la cobertura en el cálculo de KPIs en `TareaServiceImpl`.
5. **Captura 5 (Consola de Maven):** Salida final de la consola con la leyenda `BUILD SUCCESS` tras correr `.\mvnw.cmd clean verify` en el microservicio `ms-tareas`.

---

## 9. Conclusiones
* **Cumplimiento Total:** Se ha cumplido con el 100% de los requerimientos técnicos fijados por la evaluación EV3 referentes a la cobertura mínima de código del backend (Gate de Calidad de 60%).
* **Robustez del Código:** Las 68 pruebas automatizadas otorgan un alto nivel de confianza al equipo de desarrollo, confirmando que las integraciones, cálculos de KPIs de tareas y configuraciones de seguridad funcionan exactamente como se espera.
* **Independencia en H2:** El aislamiento de las bases de datos en memoria previene la corrupción de datos y mantiene los ciclos de compilación y verificación sumamente rápidos (menos de 15 segundos por componente).

---

## 10. Recomendaciones
1. **Mantener la Regla de Cobertura en el Pipeline:** La regla de control de JaCoCo (`check` con 60% mínimo) debe mantenerse activa en los builds locales y de integración continua para evitar que nuevas implementaciones degraden la calidad.
2. **Expandir Pruebas en Controladores:** Si bien el servicio tiene excelente cobertura, incrementar los casos de prueba de controladores REST API mediante `MockMvc` para simular llamadas web completas (códigos de estado no exitosos 400, 401, 404, etc.) aumentará aún más el porcentaje general.
3. **Automatización del Proceso:** Se recomienda configurar una acción de GitHub (GitHub Actions) que corra automáticamente `.\mvnw.cmd clean verify` en cada Pull Request para asegurar que ningún merge rompa las pruebas ni disminuya la cobertura permitida.
