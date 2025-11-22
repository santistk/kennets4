#!/bin/bash

# ========================================
#  EJECUTAR - MathSolver Pro
# ========================================

echo ""
echo "========================================"
echo "   MathSolver Pro - Calculadora"
echo "========================================"
echo ""
echo "Iniciando aplicación..."
echo ""
echo "La calculadora se abrirá en tu navegador"
echo "URL: http://localhost:8501"
echo ""
echo "Para CERRAR la aplicación, presiona Ctrl+C"
echo ""
echo "========================================"
echo ""

# Ejecutar la aplicación
python3 -m streamlit run app.py

# Si hay error
if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] No se pudo iniciar la aplicación"
    echo ""
    echo "Posibles soluciones:"
    echo "1. Ejecuta primero ./INSTALAR.sh"
    echo "2. Verifica que Python esté instalado"
    echo "3. Verifica que estés en la carpeta correcta"
    echo ""
fi