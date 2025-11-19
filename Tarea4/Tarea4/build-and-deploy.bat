@echo off
echo Compilando proyecto ShopLite...
call mvn clean package

if %ERRORLEVEL% NEQ 0 (
    echo Error al compilar el proyecto
    pause
    exit /b 1
)

echo.
echo Compilacion exitosa!
echo.
echo Copiando WAR a WildFly...
copy /Y target\shoplite.war "C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final\standalone\deployments\"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Despliegue completado!
    echo La aplicacion estara disponible en: http://localhost:8080/shoplite/
) else (
    echo.
    echo Error al copiar el archivo WAR
)

pause

