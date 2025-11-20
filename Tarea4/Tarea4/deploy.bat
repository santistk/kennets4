@echo off
echo ========================================
echo   Desplegando ShopLite en WildFly
echo ========================================
echo.

REM Ruta al archivo WAR compilado
set WAR_FILE=target\shoplite.war

REM Ruta a la carpeta de deployments de WildFly
set DEPLOY_PATH=C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final\standalone\deployments

REM Verificar que el archivo WAR existe
if not exist "%WAR_FILE%" (
    echo [ERROR] No se encontró el archivo WAR: %WAR_FILE%
    echo Por favor, compila el proyecto primero con: mvn clean package
    pause
    exit /b 1
)

echo [1/2] Copiando WAR a deployments...
copy /Y "%WAR_FILE%" "%DEPLOY_PATH%\shoplite.war"

if errorlevel 1 (
    echo [ERROR] No se pudo copiar el archivo WAR
    pause
    exit /b 1
)

echo [2/2] Archivo copiado exitosamente
echo.
echo ========================================
echo   Despliegue completado!
echo ========================================
echo.
echo El archivo shoplite.war ha sido copiado a:
echo %DEPLOY_PATH%
echo.
echo WildFly detectará automáticamente el cambio y redesplegará la aplicación.
echo Revisa los logs de WildFly para confirmar el despliegue.
echo.
pause

