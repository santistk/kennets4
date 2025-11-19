# Instrucciones para Ejecutar ShopLite

## Pasos para Ejecutar la Aplicación

### Paso 1: Iniciar WildFly

Primero, necesitas iniciar el servidor WildFly. Tienes dos opciones:

#### Opción A: Usar el script proporcionado
Ejecuta el archivo `start-wildfly.bat` (doble clic o desde la terminal)

#### Opción B: Iniciar manualmente
1. Abre una terminal (PowerShell o CMD)
2. Navega a la carpeta de WildFly:
   ```cmd
   cd C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final\bin
   ```
3. Ejecuta:
   ```cmd
   standalone.bat
   ```

**Espera hasta que veas el mensaje:**
```
WFLYSRV0025: WildFly Full 36.0.0.Final started
```

⚠️ **IMPORTANTE:** Deja esta ventana abierta mientras uses la aplicación.

---

### Paso 2: Compilar y Desplegar la Aplicación

Tienes dos opciones:

#### Opción A: Usar el script automático (Recomendado)
Ejecuta el archivo `build-and-deploy.bat` (doble clic o desde la terminal)

Este script:
- Compila el proyecto con Maven
- Copia el archivo WAR a la carpeta de deployments de WildFly
- WildFly detectará automáticamente el archivo y lo desplegará

#### Opción B: Hacerlo manualmente

1. **Compilar el proyecto:**
   Abre una nueva terminal en la carpeta del proyecto y ejecuta:
   ```cmd
   mvn clean package
   ```

2. **Copiar el WAR a WildFly:**
   ```cmd
   copy target\shoplite.war "C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final\standalone\deployments\"
   ```

3. **Verificar el despliegue:**
   En la consola de WildFly deberías ver mensajes como:
   ```
   WFLYSRV0010: Deployed "shoplite.war"
   ```

---

### Paso 3: Acceder a la Aplicación

1. Abre tu navegador web
2. Ve a la siguiente URL:
   ```
   http://localhost:8080/shoplite/
   ```

3. Deberías ver la página principal de ShopLite

---

### Paso 4: Probar la Aplicación

#### Iniciar Sesión

Puedes usar estos usuarios de prueba:

**Administrador:**
- Email: `admin@shoplite.com`
- Contraseña: `admin123`

**Usuario Normal:**
- Email: `user@shoplite.com`
- Contraseña: `user123`

#### Funcionalidades a Probar:

1. **Como Usuario Normal:**
   - Iniciar sesión con `user@shoplite.com`
   - Ver la lista de productos (paginada)
   - Cerrar sesión

2. **Como Administrador:**
   - Iniciar sesión con `admin@shoplite.com`
   - Ver la lista de productos
   - Acceder al panel de administración (botón "Panel de Administración")
   - Crear nuevos productos
   - Verificar que los productos aparecen en la lista

3. **Probar Seguridad:**
   - Intentar acceder a `/home` sin estar logueado (debe redirigir a login)
   - Como usuario normal, intentar acceder a `/admin` (debe mostrar error 403)

---

## Solución de Problemas

### Error: "Puerto 8080 ya está en uso"
- WildFly ya está corriendo, no necesitas iniciarlo de nuevo
- O cierra la aplicación que está usando el puerto 8080

### Error: "No se puede conectar a localhost:8080"
- Verifica que WildFly esté corriendo
- Revisa la consola de WildFly para ver si hay errores

### Error al compilar con Maven
- Verifica que tengas Maven instalado: `mvn --version`
- Verifica que tengas Java 21: `java -version`
- Asegúrate de estar en la carpeta del proyecto

### La aplicación no se despliega
- Verifica que el archivo `shoplite.war` esté en la carpeta `standalone/deployments`
- Revisa los logs de WildFly para ver errores
- Verifica que no haya errores de compilación

### Error 404 al acceder a la aplicación
- Verifica que el despliegue fue exitoso en los logs de WildFly
- Asegúrate de usar la URL correcta: `http://localhost:8080/shoplite/`
- Verifica que el contexto de la aplicación sea `/shoplite`

---

## Detener la Aplicación

1. **Detener WildFly:**
   - En la consola de WildFly, presiona `Ctrl + C`
   - O cierra la ventana de la consola

2. **Redesplegar (si haces cambios):**
   - Ejecuta `build-and-deploy.bat` nuevamente
   - WildFly detectará el cambio y redesplegará automáticamente

---

## Comandos Útiles

### Verificar que WildFly está corriendo
```cmd
netstat -an | findstr :8080
```

### Ver logs de WildFly
Los logs están en:
```
C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final\standalone\log\server.log
```

### Desplegar usando CLI de WildFly
```cmd
cd C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final\bin
jboss-cli.bat --connect --command="deploy C:\Programacion IV\Tarea3\target\shoplite.war"
```

### Undeploy (eliminar aplicación)
```cmd
cd C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final\bin
jboss-cli.bat --connect --command="undeploy shoplite.war"
```

