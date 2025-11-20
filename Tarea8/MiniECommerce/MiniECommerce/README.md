# Project Task Manager

Aplicación web para gestión de proyectos y tareas desarrollada con Jakarta EE 10, PrimeFaces 10 y CDI.

## Versiones

### JDK
- **Java**: 21.0.8
- **Maven Compiler**: 21 (source y target)

### Servidor de Aplicaciones
- **WildFly**: 36.0.0.Final
- **Ruta de instalación**: `C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final`

### Tecnologías y Frameworks
- **Jakarta EE**: 10.0.0
- **PrimeFaces**: 10.0.0 (Jakarta)
- **PrimeFlex**: 2.0.0
- **PrimeFaces Themes**: 15.0.7
- **Maven**: 3.x

## Cómo Ejecutar

### Prerrequisitos
1. Java JDK 21.0.8 instalado
2. Maven 3.x instalado
3. WildFly 36.0.0.Final instalado en `C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final`

### Pasos para Ejecutar

#### 1. Iniciar WildFly

Abre una terminal y ejecuta:

```powershell
cd "C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final\bin"
.\standalone.bat
```

Espera hasta que aparezca el mensaje:
```
WFLYSRV0025: WildFly Full 36.0.0.Final started
```

#### 2. Compilar y Desplegar el Proyecto

En otra terminal, navega al directorio del proyecto y ejecuta:

```powershell
cd "C:\Programacion IV\Tarea8\MiniECommerce\MiniECommerce"
mvn clean package wildfly:deploy
```

Este comando:
- Limpia el proyecto anterior
- Compila el código fuente
- Empaqueta la aplicación en un archivo WAR
- Despliega automáticamente en WildFly

#### 3. Acceder a la Aplicación

Abre tu navegador y accede a:

**URL Principal:**
```
http://localhost:8080/ProjectTaskManager/
```

**URL Directa a Proyectos:**
```
http://localhost:8080/ProjectTaskManager/pages/projects.xhtml
```

### Comandos Útiles

#### Redesplegar (después de cambios)
```powershell
mvn clean package wildfly:redeploy
```

#### Desplegar sin compilar
```powershell
mvn wildfly:deploy
```

#### Desplegar solo (si ya está compilado)
```powershell
mvn wildfly:deploy-only
```

#### Detener el despliegue
```powershell
mvn wildfly:undeploy
```

### Detener WildFly

Para detener el servidor WildFly, presiona `Ctrl+C` en la terminal donde está corriendo, o cierra la ventana de la terminal.

## Estructura del Proyecto

```
MiniECommerce/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/darwinruiz/miniecommerce/
│   │   │       ├── controllers/    # Controladores JSF/CDI
│   │   │       ├── models/         # Entidades (Project, Task)
│   │   │       ├── repositories/   # Repositorios en memoria
│   │   │       ├── services/       # Lógica de negocio
│   │   │       └── validators/     # Validadores personalizados
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   │       └── beans.xml       # Configuración CDI
│   │   └── webapp/
│   │       ├── pages/              # Páginas XHTML
│   │       ├── WEB-INF/
│   │       │   ├── web.xml         # Configuración web
│   │       │   └── beans.xml       # Configuración CDI
│   │       └── index.xhtml         # Página de inicio
│   └── pom.xml                     # Configuración Maven
└── README.md
```

## Funcionalidades

- ✅ Gestión de Proyectos (CRUD completo)
- ✅ Gestión de Tareas por Proyecto (CRUD completo)
- ✅ Búsqueda y filtrado de proyectos
- ✅ Validaciones personalizadas
- ✅ Interfaz responsive con PrimeFaces
- ✅ Operaciones AJAX para mejor experiencia de usuario

