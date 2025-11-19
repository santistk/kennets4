@echo off
echo ========================================
echo Iniciando WildFly 36.0.0
echo ========================================
echo.

cd /d "C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final\bin"

if not exist "standalone.bat" (
    echo ERROR: No se encontro standalone.bat en la ruta especificada
    echo Verifica que WildFly este instalado en:
    echo C:\Wildfly\Wildfly\wildfly-36.0.0.Final\wildfly-36.0.0.Final\bin
    pause
    exit /b 1
)

echo Iniciando servidor WildFly...
echo.
echo Espera hasta ver el mensaje: "WFLYSRV0025: WildFly Full 36.0.0.Final started"
echo.
echo IMPORTANTE: Deja esta ventana abierta mientras uses la aplicacion
echo Para detener el servidor, presiona Ctrl+C
echo.
echo ========================================
echo.

call standalone.bat

