#!/bin/bash

# ========================================
#  INSTALADOR - MathSolver Pro
# ========================================

echo ""
echo "========================================"
echo "   INSTALADOR - MathSolver Pro"
echo "========================================"
echo ""

# Verificar Python
echo "[1/3] Verificando Python..."
if ! command -v python3 &> /dev/null; then
    echo ""
    echo "[ERROR] Python no está instalado"
    echo ""
    echo "Instala Python con:"
    echo "  Mac: brew install python"
    echo "  Ubuntu/Debian: sudo apt install python3 python3-pip"
    echo "  Fedora: sudo dnf install python3 python3-pip"
    echo ""
    exit 1
fi
echo "[OK] Python encontrado: $(python3 --version)"
echo ""

# Actualizar pip
echo "[2/3] Actualizando pip..."
python3 -m pip install --upgrade pip --quiet --user
echo "[OK] pip actualizado"
echo ""

# Instalar dependencias
echo "[3/3] Instalando dependencias (esto puede tardar 1-2 minutos)..."
python3 -m pip install -r requirements.txt --quiet --user
if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] No se pudieron instalar las dependencias"
    echo "Verifica tu conexión a internet e intenta de nuevo"
    echo ""
    exit 1
fi
echo "[OK] Dependencias instaladas"
echo ""

echo "========================================"
echo "   INSTALACIÓN COMPLETADA"
echo "========================================"
echo ""
echo "La calculadora está lista para usar"
echo ""
echo "Para ejecutarla, usa: ./EJECUTAR.sh"
echo ""