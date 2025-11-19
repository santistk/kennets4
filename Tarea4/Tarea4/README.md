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
   - Protege las rutas `/admin/*`
   - Valida sesión activa
   - Verifica que el rol sea "ADMIN"
   - Hace forward a 403.jsp si no cumple los requisitos

### Servlets

1. **LoginServlet** (`src/main/java/com/darwinruiz/shoplite/controllers/LoginServlet.java`)
   - Procesa el inicio de sesión
   - Valida credenciales usando UserRepository (JDBC)
   - Crea nueva sesión con atributos: `auth`, `username`, `role`
   - Configura `maxInactiveInterval` a 30 minutos
   - Redirige a `/home` si es exitoso, a `login.jsp?err=1` si falla

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
   - Redirige a `/home` si es exitoso, a `/admin?err=1` si falla

## Configuración de Base de Datos

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

### 3. Configurar conexión

Editar `src/main/java/com/darwinruiz/shoplite/database/DbConnection.java` y ajustar:
- `DB_URL`: URL de conexión (por defecto: `jdbc:postgresql://localhost:5432/shoplite`)
- `DB_USER`: Usuario de PostgreSQL (por defecto: `postgres`)
- `DB_PASSWORD`: Contraseña de PostgreSQL (por defecto: `postgres`)

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

## Compilación y Despliegue

### 1. Compilar el proyecto

```bash
mvn clean package
```

Esto generará el archivo WAR en `target/shoplite.war`

### 2. Desplegar en WildFly

El WildFly está ubicado en: `C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final\bin`

#### Opción 1: Copiar manualmente
1. Copiar `target/shoplite.war` a `C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final\standalone\deployments\`
2. WildFly detectará automáticamente el archivo y lo desplegará

#### Opción 2: Usar CLI de WildFly
```bash
cd C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final\bin
jboss-cli.bat --connect --command="deploy C:\Programacion IV\Tarea3\target\shoplite.war"
```

### 3. Acceder a la aplicación

Una vez desplegada, acceder a:
- `http://localhost:8080/shoplite/`

## Notas Técnicas

- El proyecto usa Jakarta EE (anteriormente Java EE)
- Los filtros están configurados tanto con anotaciones `@WebFilter` como en `web.xml` para mayor compatibilidad
- Las sesiones tienen un tiempo de inactividad de 30 minutos
- **Persistencia:** Los datos se almacenan en PostgreSQL usando JDBC puro
- **Arquitectura:** Controllers → Repositories (JDBC) → PostgreSQL
- **Conexión:** Singleton pattern en `DbConnection` para conexión centralizada
- **Repositorios:** Interfaces (`IUserRepository`, `IProductRepository`) con implementaciones JDBC
- **Paginación:** Implementada a nivel de base de datos para mejor rendimiento

