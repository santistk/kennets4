# Entregables - ShopLite con PostgreSQL

Este documento lista todos los entregables del proyecto ShopLite migrado a PostgreSQL.

## ✅ Entregables Completados

### 1. Proyecto Completo con Actividades Desarrolladas

El proyecto incluye:

- **Migración completa a PostgreSQL con JDBC puro**
- **Arquitectura por capas:** Controllers → Services → Repositories (JDBC)
- **Conexión centralizada:** Clase `DbConnection` en modo singleton
- **Repositorios con interfaces:** `IUserRepository` e `IProductRepository`
- **CRUD completo de productos** con paginación a nivel de base de datos
- **Filtros de seguridad:** `AuthFilter` y `AdminFilter` conservados
- **Vistas JSP actualizadas** para trabajar con PostgreSQL

**Estructura del proyecto:**
```
shoplite/
├── src/main/java/com/darwinruiz/shoplite/
│   ├── controllers/     (Servlets)
│   ├── services/        (Capa de servicios)
│   ├── repositories/    (Interfaces + Implementaciones JDBC)
│   ├── models/          (User, Product)
│   ├── database/        (DbConnection)
│   └── filters/         (AuthFilter, AdminFilter)
├── src/main/webapp/     (JSPs)
├── database/
│   └── init.sql         (Script SQL)
├── pom.xml
├── README.md
└── USUARIOS_PRUEBA.md
```

### 2. Repositorio Público de Github

**Instrucciones para subir a GitHub:**

1. Inicializar repositorio (si no está inicializado):
   ```bash
   git init
   git add .
   git commit -m "Migración ShopLite a PostgreSQL con JDBC"
   ```

2. Crear repositorio en GitHub Classroom

3. Conectar y subir:
   ```bash
   git remote add origin <URL_DEL_REPOSITORIO>
   git branch -M main
   git push -u origin main
   ```

**Nota:** El archivo `.gitignore` está incluido para excluir archivos compilados.

### 3. Usuarios de Prueba

Los usuarios de prueba están documentados en:
- **README.md** (sección "Usuarios de Prueba")
- **USUARIOS_PRUEBA.md** (documento dedicado)

**Credenciales:**

**Administrador:**
- Username: `admin`
- Password: `admin123`
- Rol: `ADMIN`

**Usuario Regular:**
- Username: `alice`
- Password: `alice123`
- Rol: `USER`

**Usuario Regular (alternativo):**
- Username: `bob`
- Password: `bob123`
- Rol: `USER`

### 4. Script SQL de Creación de Tablas y Datos de Ejemplo

**Ubicación:** `database/init.sql`

**Contenido:**
- Creación de tabla `users` con campos: id, username, password, role, active, created_at
- Creación de tabla `products` con campos: id, name, price, stock, created_at
- Datos de ejemplo:
  - 3 usuarios (admin, alice, bob)
  - 3 productos (Teclado, Mouse, Monitor)

**Cómo ejecutar:**
```bash
psql -U postgres -d shoplite -f database/init.sql
```

O desde psql:
```sql
\c shoplite
\i database/init.sql
```

## Verificación de Cumplimiento

✅ **Migración a PostgreSQL:** Completada  
✅ **JDBC puro:** Implementado (sin frameworks ORM)  
✅ **Arquitectura por capas:** Controllers → Services → Repositories  
✅ **Conexión centralizada:** DbConnection singleton  
✅ **Interfaces de repositorios:** IUserRepository, IProductRepository  
✅ **CRUD completo:** Productos con paginación  
✅ **Filtros de seguridad:** AuthFilter y AdminFilter conservados  
✅ **Vistas JSP:** Actualizadas para BD  
✅ **Script SQL:** Incluido con tablas y datos de ejemplo  
✅ **Usuarios de prueba:** Documentados (admin/admin123 y alice/alice123)  

## Archivos Importantes

- `database/init.sql` - Script SQL con esquema y datos iniciales
- `src/main/java/com/darwinruiz/shoplite/database/DbConnection.java` - Conexión a BD
- `README.md` - Documentación completa del proyecto
- `USUARIOS_PRUEBA.md` - Documentación de usuarios de prueba
- `pom.xml` - Dependencias (incluye PostgreSQL JDBC)

## Notas Finales

- El proyecto está listo para compilar con `mvn clean package`
- La base de datos debe crearse y ejecutarse el script antes de desplegar
- Las credenciales de conexión están en `DbConnection.java` (ajustar según entorno)
- Todos los requerimientos han sido implementados y probados

