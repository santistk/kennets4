# ExamenFinal Pro - Calculadora de Ecuaciones

Resuelve ecuaciones **algebraicas** y **diferenciales** con explicaciones paso a paso.

---

## Inicio Rápido

### Windows
1. Descarga todos los archivos
2. Haz doble clic en **`INSTALAR.bat`**
3. Haz doble clic en **`EJECUTAR.bat`**
4. ¡Listo! Se abrirá en tu navegador

### Mac / Linux
1. Descarga todos los archivos
2. Abre Terminal en la carpeta del proyecto
3. Ejecuta: `./INSTALAR.sh`
4. Ejecuta: `./EJECUTAR.sh`
5. ¡Listo! Se abrirá en tu navegador

📖 **¿Primera vez con Python?** Lee [INSTALACION.md](INSTALACION.md) para instrucciones detalladas.

---

## ✨ Características

### Ecuaciones Algebraicas
- **Resolución paso a paso**: Ve cada operación algebraica explicada
- **Validación inteligente**: Detecta errores y sugiere correcciones
- **Casos especiales**: Maneja ecuaciones sin solución e infinitas soluciones

### Ecuaciones Diferenciales
- **Primer orden**: Resuelve EDOs de primer orden
- **Tipos múltiples**: Separables, lineales, homogéneas
- **Solución general**: Muestra la familia de soluciones con constantes

### 🌟 Interfaz Universal
- **Responsive**: Funciona en móvil y desktop
- **Ejemplos interactivos**: Prueba ecuaciones predefinidas
- **Visualización LaTeX**: Ecuaciones renderizadas matemáticamente
- **Selector de tipo**: Cambia fácilmente entre algebraicas y diferenciales

## Requisitos

- **Python 3.8 o superior**
- **Conexión a internet** (solo para la instalación)

Las dependencias se instalan automáticamente:
- SymPy 1.12 (matemática simbólica)
- Streamlit 1.28.1 (interfaz web)

## 📖 Cómo Usar

### Formato de Ecuaciones

#### Ecuaciones Algebraicas
```
Formatos válidos:
2*x + 3 = 7
x - 5 = 10
3*x = 12
-2*x + 8 = 2
5 = 2*x + 1
2*(x + 1) = 6

Formatos no válidos:
x^2 + 2*x = 5    (no es primer grado)
2x + 3 = 7       (falta el * para multiplicación)
x + = 5          (ecuación incompleta)
```

#### 📈 Ecuaciones Diferenciales
```
Formatos válidos:
dy/dx = 3*x**2
dy/dx = 2*y
y' = sin(x)
dy/dx = x + 1
dy/dx + 2*y = 0
y' = exp(x)

Formatos no válidos:
d²y/dx² + y = 0  (segundo orden - próximamente)
dy/dt = y        (variable independiente debe ser x)
```

### Casos Especiales

La calculadora maneja automáticamente:

- **Sin solución**: `0*x + 5 = 3` → No existe valor de x que satisfaga la ecuación
- **Infinitas soluciones**: `0*x + 5 = 5` → Cualquier valor de x es válido
- **Solución única**: `2*x + 3 = 7` → x = 2

## Ejemplos de Uso

### Ejemplo 1: Ecuación Algebraica
**Entrada:** `2*x + 3 = 7`

**Salida:**
```
Solución: x = 2

📝 Pasos de resolución:
Paso 1: Ecuación original → 2*x + 3 = 7
Paso 2: Mover términos → 2*x + 3 - 7 = 0
Paso 3: Simplificar → 2*x - 4 = 0
Paso 4: Solución final → x = 2
```

### Ejemplo 2: Ecuación Diferencial Separable
**Entrada:** `dy/dx = 3*x**2`

**Salida:**
```
Solución General: y(x) = C1 + x³

Tipo: primer orden separable

📝 Pasos de resolución:
Paso 1: Ecuación diferencial original → dy/dx = 3*x²
Paso 2: Identificación del tipo → primer orden separable
Paso 3: Solución general → y(x) = C1 + x³
```

### Ejemplo 3: Ecuación Diferencial Lineal
**Entrada:** `dy/dx = 2*y`

**Salida:**
```
olución General: y(x) = C1*exp(2*x)

Tipo: primer orden lineal
```

## Arquitectura Técnica

### Componentes Principales

- **`equation_parser.py`**: Parser inteligente con SymPy
- **`equation_solver.py`**: Motor de resolución con pasos detallados
- **`app.py`**: Interfaz web con Streamlit

### Flujo de Datos

```
Entrada del Usuario → Validación → Parsing → Resolución → Visualización
```

## Testing

Para probar la aplicación, puedes usar estos casos de prueba:

```python
# Casos básicos
"2*x + 3 = 7"     # → x = 2
"x - 5 = 10"      # → x = 15
"3*x = 12"        # → x = 4

# Casos edge
"0*x + 5 = 5"     # → Infinitas soluciones
"0*x + 5 = 3"     # → Sin solución
"x = x + 1"       # → Sin solución

# Casos complejos
"2*(x + 1) = 6"   # → x = 2
"-3*x + 9 = 0"    # → x = 3
```

## Personalización

### Agregar Nuevos Tipos de Ecuación

Para extender la funcionalidad a ecuaciones cuadráticas:

1. Modifica `equation_solver.py` para detectar grado 2
2. Implementa `QuadraticEquationSolver`
3. Actualiza la interfaz en `app.py`

### Cambiar Estilos

Modifica la función `apply_custom_css()` en `app.py` para personalizar:
- Colores del tema
- Tipografía
- Layout responsive

## Desarrolladores

- **Marlon Estuardo Pappa Hernández**
- **Gustavo Adolfo Ortiz Gutiérrez**
- **Kennet Anderson Santisteban Torres**
- **Jonathan Javier Soberanis Castillo**
- **Javier Augusto Estrada Gordillo**
