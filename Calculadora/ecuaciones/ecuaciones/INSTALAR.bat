@echo off
REM ========================================
REM  INSTALADOR - MathSolver Pro
REM ========================================

echo.
echo ========================================
echo   INSTALADOR - MathSolver Pro
echo ========================================
echo.

REM Verificar Python
echo [1/3] Verificando Python...
python --version >nul 2>&1
if errorlevel 1 (
    echo.
    echo [ERROR] Python no esta instalado
    echo.
    echo Por favor instala Python desde: https://www.python.org/downloads/
    echo IMPORTANTE: Marca la casilla "Add Python to PATH" durante la instalacion
    echo.
    pause
    exit /b 1
)
echo [OK] Python encontrado
echo.

REM Actualizar pip
echo [2/3] Actualizando pip...
python -m pip install --upgrade pip --quiet
echo [OK] pip actualizado
echo.

REM Instalar dependencias
echo [3/3] Instalando dependencias (esto puede tardar 1-2 minutos)...
python -m pip install -r requirements.txt --quiet
if errorlevel 1 (
    echo.
    echo [ERROR] No se pudieron instalar las dependencias
    echo Verifica tu conexion a internet e intenta de nuevo
    echo.
    pause
    exit /b 1
)
echo [OK] Dependencias instaladas
echo.

echo ========================================
echo   INSTALACION COMPLETADA
echo ========================================
echo.
echo La calculadora esta lista para usar
echo.
echo Para ejecutarla, haz doble clic en: EJECUTAR.bat
echo.
pause