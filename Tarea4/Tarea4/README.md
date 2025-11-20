# ShopLite - Aplicación Web Java

Aplicación web desarrollada con Java 21.0.8 y desplegada en WildFly 36.0.0.

## Requisitos

- Java 21.0.8
- WildFly 36.0.0 Final
- Maven 3.6+
- PostgreSQL 12+

## Estructura del Proyecto

```
shoplite/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/darwinruiz/shoplite/
│   │   │       ├── controllers/
│   │   │       │   ├── LoginServlet.java
│   │   │       │   ├── LogoutServlet.java
│   │   │       │   ├── HomeServlet.java
│   │   │       │   └── AdminServlet.java
│   │   │       ├── filters/
│   │   │       │   ├── AuthFilter.java
│   │   │       │   └── AdminFilter.java
│   │   │       ├── database/
│   │   │       │   └── DbConnection.java
│   │   │       ├── models/
│   │   │       │   ├── User.java
│   │   │       │   └── Product.java
│   │   │       ├── services/
│   │   │       │   ├── UserService.java
│   │   │       │   └── ProductService.java
│   │   │       └── repositories/
│   │   │           ├── IUserRepository.java
│   │   │           ├── IProductRepository.java
│   │   │           ├── UserRepository.java
│   │   │           └── ProductRepository.java
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml
│   │       ├── index.jsp
│   │       ├── login.jsp
│   │       ├── home.jsp
│   │       ├── admin.jsp
│   │       └── 403.jsp
│   └── pom.xml
└── README.md
```

## Componentes Implementados

### Filtros

1. **AuthFilter** (`src/main/java/com/darwinruiz/shoplite/filters/AuthFilter.java`)
   - Protege todas las páginas privadas
   - Permite acceso a páginas públicas (index.jsp, login.jsp, /auth/login, /)
   - Valida sesión activa con atributo `auth = true`
   - Redirige a login.jsp si no hay sesión válida

2. **AdminFilter** (`src/main/java/com/darwinruiz/shoplite/filters/AdminFilter.java`)
   - Protege las rutas `/app/users/*`
   - Valida sesión activa
   - Verifica que el rol sea "ADMIN"
   - Hace forward a 403.jsp si no cumple los requisitos

### Servlets

1. **LoginServlet** (`src/main/java/com/darwinruiz/shoplite/controllers/LoginServlet.java`)
   - Procesa el inicio de sesión
   - Valida credenciales usando UserRepository (JDBC)
   - Crea nueva sesión con atributos: `auth`, `username`, `role`
   - Configura `maxInactiveInterval` a 30 minutos
   - Redirige a `/app/home` si es exitoso, a `login.jsp?err=1` si falla

2. **LogoutServlet** (`src/main/java/com/darwinruiz/shoplite/controllers/LogoutServlet.java`)
   - Invalida la sesión activa
   - Redirige a `index.jsp?bye=1`

3. **HomeServlet** (`src/main/java/com/darwinruiz/shoplite/controllers/HomeServlet.java`)
   - Muestra productos con paginación desde PostgreSQL
   - Parámetros: `page` y `size`
   - Envía a la vista: `items`, `page`, `size`, `total`, `totalPages`

4. **AdminServlet** (`src/main/java/com/darwinruiz/shoplite/controllers/AdminServlet.java`)
   - GET: Muestra formulario de creación
   - POST: Crea nuevo producto en PostgreSQL
   - Valida nombre no vacío, precio > 0 y stock >= 0
   - El ID se genera automáticamente por la base de datos
   - Redirige a `/app/home` si es exitoso, a `/app/users?err=1` si falla

## Configuración de Base de Datos

### Opción A: Usando Docker (Recomendado) 🐳

**La forma más fácil de configurar PostgreSQL:**

1. **Asegúrate de tener Docker Desktop instalado y corriendo**

2. **Inicia PostgreSQL:**
   ```bash
   docker-compose up -d
   ```
   
   O simplemente ejecuta: `start-database.bat`

3. **¡Listo!** El script SQL se ejecuta automáticamente. Tu aplicación ya puede conectarse.

**Comandos útiles:**
- Ver estado: `docker-compose ps`
- Ver logs: `docker-compose logs postgres`
- Detener: `docker-compose down` (o `stop-database.bat`)
- Reiniciar: `docker-compose restart`


### Opción B: Instalación Local de PostgreSQL

### 1. Crear la base de datos PostgreSQL

```sql
CREATE DATABASE shoplite;
```

### 2. Ejecutar el script de inicialización

```bash
psql -U postgres -d shoplite -f database/init.sql
```

O desde psql:

```sql
\c shoplite
\i database/init.sql
```

### 3. Configurar conexión (si es necesario)

Por defecto, la aplicación se conecta a:
- URL: `jdbc:postgresql://localhost:5433/shoplite` (puerto 5433 para evitar conflictos)
- Usuario: `postgres`
- Contraseña: `postgres`

Si necesitas cambiar esto, edita `src/main/java/com/darwinruiz/shoplite/database/DbConnection.java`

## Usuarios de Prueba

- **Administrador:**
  - Username: `admin`
  - Password: `admin123`
  - Rol: `ADMIN`

- **Usuario:**
  - Username: `alice`
  - Password: `alice123`
  - Rol: `USER`

- **Usuario:**
  - Username: `bob`
  - Password: `bob123`
  - Rol: `USER`

## Inicio Rápido

### 1. Iniciar PostgreSQL (Docker)

```bash
docker-compose up -d
```

O ejecuta: `start-database.bat`

**Nota:** Si tienes PostgreSQL local en el puerto 5432, el contenedor usa el puerto 5433 automáticamente.

### 2. Compilar y Desplegar

```bash
mvn clean package
```

Copia `target/shoplite.war` a la carpeta `deployments` de WildFly, o usa el script `build-and-deploy.bat`.

### 3. Iniciar WildFly

Ejecuta `start-wildfly.bat` o inicia WildFly manualmente.

### 4. Acceder a la aplicación

- URL: `http://localhost:8080/shoplite/`
- Login: `admin` / `admin123` (ADMIN) o `alice` / `alice123` (USER)

## Solución de Problemas

**Error de conexión a PostgreSQL:**
- Verifica que Docker esté corriendo: `docker ps`
- Si hay PostgreSQL local, el contenedor usa puerto 5433
- Recrea el contenedor: `docker-compose down -v && docker-compose up -d`

**Error de autenticación:**
- La contraseña por defecto es `postgres` (configurada en `DbConnection.java`)
- Si cambias la contraseña en Docker, actualiza `DbConnection.java` y recompila

## Notas Técnicas

- El proyecto usa Jakarta EE (anteriormente Java EE)
- Los filtros están configurados tanto con anotaciones `@WebFilter` como en `web.xml` para mayor compatibilidad
- Las sesiones tienen un tiempo de inactividad de 30 minutos
- **Persistencia:** Los datos se almacenan en PostgreSQL usando JDBC puro
- **Arquitectura:** Controllers → Services → Repositories (JDBC) → PostgreSQL
- **Rutas:** `/app/*` protegidas por AuthFilter, `/app/users/*` protegidas por AdminFilter
- **Conexión:** Singleton pattern en `DbConnection` para conexión centralizada
- **Repositorios:** Interfaces (`IUserRepository`, `IProductRepository`) con implementaciones JDBC
- **Paginación:** Implementada a nivel de base de datos para mejor rendimiento

