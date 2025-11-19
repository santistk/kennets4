# ShopLite - Aplicación Web Java

Aplicación web desarrollada con Java 21.0.8 y desplegada en WildFly 36.0.0.

## Requisitos

- Java 21.0.8
- WildFly 36.0.0 Final
- Maven 3.6+

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
│   │   │       ├── models/
│   │   │       │   ├── User.java
│   │   │       │   └── Product.java
│   │   │       └── repositories/
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
   - Valida credenciales usando UserRepository
   - Crea nueva sesión con atributos: `auth`, `userEmail`, `role`
   - Configura `maxInactiveInterval` a 30 minutos
   - Redirige a `/home` si es exitoso, a `login.jsp?err=1` si falla

2. **LogoutServlet** (`src/main/java/com/darwinruiz/shoplite/controllers/LogoutServlet.java`)
   - Invalida la sesión activa
   - Redirige a `index.jsp?bye=1`

3. **HomeServlet** (`src/main/java/com/darwinruiz/shoplite/controllers/HomeServlet.java`)
   - Muestra productos con paginación
   - Parámetros: `page` y `size`
   - Envía a la vista: `items`, `page`, `size`, `total`

4. **AdminServlet** (`src/main/java/com/darwinruiz/shoplite/controllers/AdminServlet.java`)
   - GET: Muestra formulario de creación
   - POST: Crea nuevo producto
   - Valida nombre no vacío y precio > 0
   - Usa `repo.nextId()` para generar ID
   - Redirige a `/home` si es exitoso, a `/admin?err=1` si falla

## Usuarios de Prueba

- **Administrador:**
  - Email: `admin@shoplite.com`
  - Password: `admin123`
  - Rol: `ADMIN`

- **Usuario:**
  - Email: `user@shoplite.com`
  - Password: `user123`
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
- Los productos se almacenan en memoria (ProductRepository)
- Los usuarios se almacenan en memoria (UserRepository)

