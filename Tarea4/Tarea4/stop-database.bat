@echo off
echo ========================================
echo   Deteniendo PostgreSQL
echo ========================================
echo.

docker-compose down

if errorlevel 1 (
    echo [ERROR] No se pudo detener el contenedor
    pause
    exit /b 1
)

echo.
echo PostgreSQL ha sido detenido correctamente.
echo.
pause

