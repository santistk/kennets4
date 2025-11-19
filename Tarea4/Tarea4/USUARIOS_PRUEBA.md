# Usuarios de Prueba - ShopLite

Este documento contiene las credenciales de los usuarios de prueba para la aplicación ShopLite.

## Usuario Administrador

- **Username:** `admin`
- **Password:** `admin123`
- **Rol:** `ADMIN`
- **Descripción:** Usuario con permisos de administrador. Puede acceder al panel de administración y crear productos.

## Usuarios Regulares

### Usuario 1
- **Username:** `alice`
- **Password:** `alice123`
- **Rol:** `USER`
- **Descripción:** Usuario regular con acceso a la lista de productos.

### Usuario 2
- **Username:** `bob`
- **Password:** `bob123`
- **Rol:** `USER`
- **Descripción:** Usuario regular con acceso a la lista de productos.

## Notas

- Todos los usuarios están activos por defecto (`active = TRUE`)
- Las contraseñas están almacenadas en texto plano (solo para fines de demostración)
- Estos usuarios se crean automáticamente al ejecutar el script `database/init.sql`

## Cómo usar

1. Acceder a la página de login: `http://localhost:8080/shoplite/login.jsp`
2. Ingresar el username y password de cualquiera de los usuarios listados arriba
3. Hacer clic en "Iniciar Sesión"

