# Guia de presentacion - Innovatech Solutions

Esta guia resume los pasos para preparar la demostracion en el notebook donde se presentara el proyecto.

## 1. Preparar el notebook

Instalar:

- Git
- Docker Desktop

En Docker Desktop usar WSL 2. No es necesario activar Windows Containers.

Verificar en PowerShell:

```powershell
docker --version
docker compose version
git --version
```

## 2. Descargar repositorios

```powershell
cd C:\Users\%USERNAME%
mkdir innovatech
cd innovatech
git clone https://github.com/Eseekaa/innovatech-backend-mercado-bello
git clone https://github.com/Eseekaa/innovatech-frontend-mercado-bello
```

## 3. Levantar el sistema

```powershell
cd C:\Users\%USERNAME%\innovatech\innovatech-backend-mercado-bello
docker compose up --build
```

Abrir:

```text
http://localhost:3000
```

## 4. Crear usuarios de prueba

Desde la web, registrar:

```text
Usuario: admin1
Email: admin@innovatech.cl
Password: @admin123
Rol inicial: Usuario
```

```text
Usuario: jefe1
Email: jefe@innovatech.cl
Password: @jefe123
Rol inicial: Usuario
```

```text
Usuario: usuario1
Email: usuario1@innovatech.cl
Password: @usuario1
Rol inicial: Usuario
```

## 5. Cambiar roles en H2

Entrar a:

```text
http://localhost:8083/h2-console
```

Datos:

```text
JDBC URL: jdbc:h2:mem:authdb
User Name: sa
Password: vacio
```

Ejecutar:

```sql
UPDATE USUARIOS SET ROL = 'ADMIN' WHERE USERNAME = 'admin1';
UPDATE USUARIOS SET ROL = 'JEFE_PROYECTO' WHERE USERNAME = 'jefe1';
SELECT ID, USERNAME, EMAIL, ROL, PASSWORD FROM USUARIOS;
```

La columna `PASSWORD` debe aparecer como texto largo encriptado, no como la contrasena real.

## 6. Flujo sugerido para mostrar

1. Iniciar sesion como `ADMIN`.
2. Crear proyectos.
3. Crear empleados.
4. Asignar uno o mas proyectos a empleados.
5. Mostrar dashboard y asignaciones.
6. Dar visto bueno y luego quitarlo.
7. Iniciar sesion como `JEFE_PROYECTO` y mostrar que puede crear/editar/asignar, pero no eliminar.
8. Iniciar sesion como `USUARIO` y mostrar que puede visualizar y dar/quitar visto bueno.

## 7. Links utiles

```text
Frontend:  http://localhost:3000
Gateway:   http://localhost:8085
BFF:       http://localhost:8084/api/bff/dashboard
Auth H2:   http://localhost:8083/h2-console
Proy H2:   http://localhost:8081/h2-console
Rec H2:    http://localhost:8082/h2-console
```

## 8. Apagar al finalizar

En la terminal de Docker:

```text
Ctrl + C
```

Luego:

```powershell
docker compose down
```

Recordatorio: H2 esta en memoria, por lo tanto los datos de prueba se borran al apagar.
