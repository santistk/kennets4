@echo off
echo ========================================
echo   Iniciando PostgreSQL con Docker
echo ========================================
echo.

REM Verificar que Docker esté corriendo
docker --version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker no está instalado o no está en el PATH
    echo Por favor, instala Docker Desktop desde: https://www.docker.com/products/docker-desktop/
    pause
    exit /b 1
)

echo [1/3] Verificando Docker...
docker ps >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker no está corriendo
    echo Por favor, inicia Docker Desktop
    pause
    exit /b 1
)

echo [2/3] Iniciando contenedor PostgreSQL...
docker-compose up -d

if errorlevel 1 (
    echo [ERROR] No se pudo iniciar el contenedor
    pause
    exit /b 1
)

echo [3/3] Esperando que PostgreSQL esté listo...
timeout /t 5 /nobreak >nul

echo.
echo ========================================
echo   PostgreSQL está corriendo!
echo ========================================
echo.
echo Contenedor: shoplite-postgres
echo Base de datos: shoplite
echo Usuario: postgres
echo Contraseña: postgres
echo Puerto: 5432
echo.
echo Para detener: docker-compose down
echo Para ver logs: docker-compose logs postgres
echo.
pause

