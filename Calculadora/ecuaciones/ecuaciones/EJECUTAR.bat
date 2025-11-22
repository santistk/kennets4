@echo off
REM ========================================
REM  EJECUTAR - MathSolver Pro
REM ========================================

echo.
echo ========================================
echo   MathSolver Pro - Calculadora
echo ========================================
echo.
echo Iniciando aplicacion...
echo.
echo La calculadora se abrira en tu navegador
echo URL: http://localhost:8501
echo.
echo Para CERRAR la aplicacion, cierra esta ventana
echo.
echo ========================================
echo.

REM Ejecutar la aplicación
python -m streamlit run app.py

REM Si hay error
if errorlevel 1 (
    echo.
    echo [ERROR] No se pudo iniciar la aplicacion
    echo.
    echo Posibles soluciones:
    echo 1. Ejecuta primero INSTALAR.bat
    echo 2. Verifica que Python este instalado
    echo 3. Verifica que estes en la carpeta correcta
    echo.
    pause
)