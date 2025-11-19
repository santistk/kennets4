# EduJSPApp - Sistema de Gestión Educativa

Aplicación web educativa desarrollada con Java 21.0.8, JSP, JSTL y Servlets, diseñada para desplegarse en WildFly 36.0.0.

## Características

- **Arquitectura MVC**: Separación clara entre Modelo, Vista y Controlador
- **Módulo CRUD de Carreras**: Gestión completa de carreras universitarias
- **Interfaz Bootstrap 5**: Diseño moderno y responsive
- **JSTL**: Uso extensivo de etiquetas JSTL para manipulación de datos
- **Plantilla Reutilizable**: Sistema de plantillas con menú lateral

## Estructura del Proyecto

```
EduJSApp/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── edu/app/
│   │   │       ├── modelo/
│   │   │       │   └── Carrera.java
│   │   │       ├── servicio/
│   │   │       │   └── CarreraService.java
│   │   │       └── controlador/
│   │   │           └── CarreraControlador.java
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml
│   │       │   ├── templates/
│   │       │   │   ├── base.jsp
│   │       │   │   └── menu.jsp
│   │       │   └── vistas/
│   │       │       ├── carreras/
│   │       │       │   ├── formulario.jsp
│   │       │       │   ├── formulario-wrapper.jsp
│   │       │       │   ├── listado.jsp
│   │       │       │   └── listado-wrapper.jsp
│   │       │       └── inicio.jsp
│   │       └── index.jsp
├── pom.xml
└── README.md
```

## Requisitos

- **Java**: 21.0.8 o superior
- **Maven**: 3.6.0 o superior
- **WildFly**: 36.0.0
- **Navegador Web**: Moderno (Chrome, Firefox, Edge, etc.)

## Instalación y Despliegue

### 1. Compilar el proyecto

```bash
mvn clean package
```

Esto generará el archivo WAR en `target/EduJSPApp.war`

### 2. Desplegar en WildFly 36.0.0

#### Opción A: Copia manual
```bash
cp target/EduJSPApp.war $WILDFLY_HOME/standalone/deployments/
```

#### Opción B: CLI de WildFly
```bash
$WILDFLY_HOME/bin/jboss-cli.sh --connect
deploy target/EduJSPApp.war
```

### 3. Acceder a la aplicación

Una vez desplegada, acceda a:
```
http://localhost:8080/EduJSPApp/
```

## Módulo de Carreras

### Funcionalidades

- **Listar**: Visualizar todas las carreras registradas
- **Crear**: Registrar nuevas carreras
- **Editar**: Modificar información de carreras existentes
- **Eliminar**: Remover carreras del sistema

### Atributos de Carrera

- **id**: Identificador autogenerado
- **nombre**: Nombre de la carrera
- **codigo**: Código interno (formato: 3 letras + 2 números)
- **facultad**: Facultad a la que pertenece
- **nivel**: Nivel académico (Técnico, Licenciatura, Maestría, Doctorado)
- **modalidades**: Lista de modalidades (Presencial, Virtual, Semipresencial)
- **estadoActiva**: Indica si la carrera está activa
- **descripcion**: Descripción general

### Tipos de Input en el Formulario

- ✅ **Texto**: Nombre, Código
- ✅ **Select**: Facultad, Nivel
- ✅ **Checkbox**: Modalidades (múltiples), Estado Activa
- ✅ **Textarea**: Descripción

## Tecnologías Utilizadas

- **Java 21**: Lenguaje de programación
- **Jakarta Servlet API 6.0**: Para servlets
- **JSP 3.1**: JavaServer Pages
- **JSTL 3.0**: JavaServer Pages Standard Tag Library
- **Bootstrap 5.3**: Framework CSS
- **Bootstrap Icons**: Iconografía

## Estructura MVC

### Modelo
- `Carrera.java`: Entidad que representa una carrera universitaria

### Vista
- JSP con JSTL para renderizado
- Plantilla base reutilizable
- Fragmentos de menú

### Controlador
- `CarreraControlador.java`: Servlet que maneja todas las peticiones CRUD

### Servicio
- `CarreraService.java`: Lógica de negocio y gestión de datos en memoria

## Uso de JSTL

El proyecto utiliza extensivamente JSTL:

- `<c:forEach>`: Para iterar sobre listas de carreras
- `<c:choose>`, `<c:when>`, `<c:otherwise>`: Para condiciones
- `<c:if>`: Para evaluaciones condicionales
- `<c:out>`: Para escape de HTML
- `<c:set>`: Para establecer variables
- `fn:length`, `fn:substring`: Funciones JSTL

## Desarrollo

### Compilar y ejecutar en modo desarrollo

```bash
mvn clean compile
```

### Verificar sintaxis

```bash
mvn validate
```

## Notas

- Los datos se almacenan en memoria (lista estática), por lo que se perderán al reiniciar el servidor
- El proyecto incluye datos de ejemplo pre-cargados
- El código está preparado para Java 21 y WildFly 36.0.0

## Autor

Proyecto educativo desarrollado para Programación IV.

