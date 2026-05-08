# Docker - Innovatech Solutions

Este archivo deja el proyecto listo para presentacion con Docker.

## Requisito

Tener Docker Desktop abierto antes de ejecutar los comandos.

## Levantar todo

Desde PowerShell:

```powershell
cd C:\Users\Eseekaa\innovatech\innovatech-backend-mercado-bello
docker compose up --build
```

Esto levanta:

- Frontend: http://localhost:3000
- API Gateway: http://localhost:8085
- BFF: http://localhost:8084
- MS Proyectos: http://localhost:8081
- MS Recursos: http://localhost:8082
- MS Auth: http://localhost:8083

## H2 Console

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

## Apagar todo

En la misma terminal donde esta corriendo Docker:

```text
Ctrl + C
```

Luego:

```powershell
docker compose down
```

## Nota importante

Las bases H2 estan en memoria. Si apagas los contenedores, los usuarios,
proyectos y empleados de prueba se borran. El codigo no se borra.
