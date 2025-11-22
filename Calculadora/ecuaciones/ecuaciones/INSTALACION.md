# 📦 Guía de Instalación - MathSolver Pro

Esta guía te ayudará a instalar y ejecutar la calculadora paso a paso, incluso si nunca has usado Python.

---

## 🪟 Para Windows

### Paso 1: Instalar Python

1. Ve a [python.org/downloads](https://www.python.org/downloads/)
2. Descarga **Python 3.8 o superior** (el botón amarillo grande)
3. **MUY IMPORTANTE:** Durante la instalación, marca la casilla **"Add Python to PATH"**
4. Haz clic en "Install Now"
5. Espera a que termine la instalación

### Paso 2: Verificar la instalación

1. Abre el **Símbolo del sistema** (busca "cmd" en el menú inicio)
2. Escribe: `python --version`
3. Deberías ver algo como: `Python 3.11.0`

### Paso 3: Instalar la calculadora

1. Descarga todos los archivos del proyecto en una carpeta
2. Abre el **Símbolo del sistema**
3. Ve a la carpeta del proyecto:
   ```
   cd C:\ruta\donde\descargaste\el\proyecto
   ```
4. Haz doble clic en el archivo **`INSTALAR.bat`**
5. Espera a que se instalen las dependencias (puede tardar 1-2 minutos)

### Paso 4: Ejecutar la calculadora

1. Haz doble clic en el archivo **`EJECUTAR.bat`**
2. Se abrirá automáticamente en tu navegador
3. ¡Listo! Ya puedes usar la calculadora

---

## 🍎 Para Mac

### Paso 1: Instalar Python

1. Abre la **Terminal** (busca "Terminal" en Spotlight)
2. Instala Homebrew (si no lo tienes):
   ```bash
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   ```
3. Instala Python:
   ```bash
   brew install python
   ```

### Paso 2: Verificar la instalación

1. En la Terminal, escribe:
   ```bash
   python3 --version
   ```
2. Deberías ver algo como: `Python 3.11.0`

### Paso 3: Instalar la calculadora

1. Descarga todos los archivos del proyecto en una carpeta
2. Abre la **Terminal**
3. Ve a la carpeta del proyecto:
   ```bash
   cd /ruta/donde/descargaste/el/proyecto
   ```
4. Ejecuta:
   ```bash
   chmod +x INSTALAR.sh
   ./INSTALAR.sh
   ```
5. Espera a que se instalen las dependencias

### Paso 4: Ejecutar la calculadora

1. En la Terminal, ejecuta:
   ```bash
   ./EJECUTAR.sh
   ```
2. Se abrirá automáticamente en tu navegador
3. ¡Listo! Ya puedes usar la calculadora

---

## 🐧 Para Linux

### Paso 1: Instalar Python

Python suele venir preinstalado en Linux. Verifica con:

```bash
python3 --version
```

Si no está instalado:

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install python3 python3-pip
```

**Fedora:**
```bash
sudo dnf install python3 python3-pip
```

### Paso 2: Instalar la calculadora

1. Descarga todos los archivos del proyecto
2. Abre la Terminal
3. Ve a la carpeta del proyecto:
   ```bash
   cd /ruta/donde/descargaste/el/proyecto
   ```
4. Ejecuta:
   ```bash
   chmod +x INSTALAR.sh
   ./INSTALAR.sh
   ```

### Paso 3: Ejecutar la calculadora

```bash
./EJECUTAR.sh
```

---

## ❓ Solución de Problemas

### "Python no se reconoce como comando"
- **Windows:** Reinstala Python y asegúrate de marcar "Add Python to PATH"
- **Mac/Linux:** Usa `python3` en lugar de `python`

### "pip no se reconoce como comando"
- Reinstala Python con la opción de incluir pip

### "Error al instalar dependencias"
- Verifica tu conexión a internet
- Intenta ejecutar manualmente:
  ```bash
  python -m pip install --upgrade pip
  python -m pip install -r requirements.txt
  ```

### "La aplicación no se abre en el navegador"
- Abre manualmente tu navegador
- Ve a: `http://localhost:8501`

### "Puerto 8501 ya está en uso"
- Cierra otras instancias de la aplicación
- O usa otro puerto:
  ```bash
  streamlit run app.py --server.port 8502
  ```

---

## 📞 ¿Necesitas Ayuda?

Si tienes problemas:

1. Verifica que Python esté instalado correctamente
2. Asegúrate de estar en la carpeta correcta del proyecto
3. Revisa que todos los archivos estén descargados
4. Consulta la sección de Solución de Problemas

---

## 🎉 ¡Disfruta de MathSolver Pro!

Una vez instalado, solo necesitas ejecutar el archivo **EJECUTAR** cada vez que quieras usar la calculadora.