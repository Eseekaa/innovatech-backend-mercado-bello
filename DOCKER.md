# Docker - Innovatech Solutions

Guia para levantar Innovatech Solutions con Docker Desktop. Esta es la forma recomendada para presentar el proyecto, porque evita abrir cinco terminales manuales con Java, Maven y Node.

## Que hace Docker en este proyecto

Docker crea un contenedor por cada parte del sistema:

- `innovatech-frontend`: aplicacion React publicada con nginx.
- `innovatech-api-gateway`: entrada central del backend.
- `innovatech-bff`: servicio que prepara datos para el frontend.
- `innovatech-ms-auth`: autenticacion, roles y JWT.
- `innovatech-ms-proyectos`: gestion de proyectos.
- `innovatech-ms-recursos`: gestion de empleados y asignaciones.

## Requisito

Docker Desktop debe estar instalado y abierto antes de ejecutar los comandos.

Para comprobarlo:

```powershell
docker --version
docker compose version
```

## Estructura necesaria

El `docker-compose.yml` esta en el repositorio backend, pero tambien construye el frontend. Por eso ambos repos deben estar dentro de la misma carpeta padre:

```text
innovatech
├── innovatech-backend-mercado-bello
└── innovatech-frontend-mercado-bello
    └── innovatech-frontend
```

## Levantar todo

Desde PowerShell:

```powershell
cd C:\Users\%USERNAME%\innovatech\innovatech-backend-mercado-bello
docker compose up --build
```

La primera vez puede demorar porque Docker descarga imagenes de Java, Maven, Node y nginx.

## URLs principales

| Componente | URL |
| --- | --- |
| Frontend | `http://localhost:3000` |
| API Gateway | `http://localhost:8085` |
| BFF | `http://localhost:8084` |
| MS Proyectos | `http://localhost:8081` |
| MS Recursos | `http://localhost:8082` |
| MS Auth | `http://localhost:8083` |

## Consolas H2

Auth:

```text
http://localhost:8083/h2-console
JDBC URL: jdbc:h2:mem:authdb
User Name: sa
Password: vacio
```

Proyectos:

```text
http://localhost:8081/h2-console
JDBC URL: jdbc:h2:mem:proyectosdb
User Name: sa
Password: vacio
```

Recursos:

```text
http://localhost:8082/h2-console
JDBC URL: jdbc:h2:mem:recursosdb
User Name: sa
Password: vacio
```

## Roles para la presentacion

Primero registrar usuarios desde la web. Luego entrar a H2 de Auth y ejecutar:

```sql
UPDATE USUARIOS SET ROL = 'ADMIN' WHERE USERNAME = 'admin1';
UPDATE USUARIOS SET ROL = 'JEFE_PROYECTO' WHERE USERNAME = 'jefe1';
UPDATE USUARIOS SET ROL = 'USUARIO' WHERE USERNAME = 'usuario1';
```

Para revisar usuarios y ver que la contrasena esta encriptada:

```sql
SELECT ID, USERNAME, EMAIL, ROL, PASSWORD FROM USUARIOS;
```

## Apagar todo

En la terminal donde esta corriendo Docker:

```text
Ctrl + C
```

Luego:

```powershell
docker compose down
```

## Reiniciar despues

Si no hubo cambios de codigo:

```powershell
cd C:\Users\%USERNAME%\innovatech\innovatech-backend-mercado-bello
docker compose up
```

Si hubo cambios de codigo:

```powershell
cd C:\Users\%USERNAME%\innovatech\innovatech-backend-mercado-bello
docker compose up --build
```

## Nota importante

Las bases H2 estan en memoria. Si apagas los contenedores, los usuarios, proyectos y empleados de prueba se borran. El codigo no se borra.
