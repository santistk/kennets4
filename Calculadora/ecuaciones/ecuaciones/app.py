"""
Calculadora de Ecuaciones de Primer Grado
Aplicación web con Streamlit y SymPy
"""

import streamlit as st
from equation_parser import EquationParser
from equation_solver import LinearEquationSolver
from differential_solver import DifferentialEquationSolver

def apply_custom_css():
    """Aplica CSS personalizado moderno y profesional"""
    st.markdown("""
    <style>
    /* Importar fuentes modernas */
    @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap');
    
    /* Variables CSS para consistencia - Paleta profesional azul/gris */
    :root {
        --primary-color: #2563eb;
        --primary-dark: #1d4ed8;
        --secondary-color: #475569;
        --success-color: #059669;
        --warning-color: #d97706;
        --error-color: #dc2626;
        --background-light: #f8fafc;
        --background-card: #ffffff;
        --text-primary: #1e293b;
        --text-secondary: #64748b;
        --border-color: #e2e8f0;
        --shadow-sm: 0 1px 2px 0 rgb(0 0 0 / 0.05);
        --shadow-md: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1);
        --shadow-lg: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
        --radius-sm: 6px;
        --radius-md: 8px;
        --radius-lg: 12px;
        --radius-xl: 16px;
    }
    
    /* Estilos globales */
    .main .block-container {
        padding-top: 2rem;
        max-width: 1200px;
    }
    
    /* Header principal moderno - Sin gradientes */
    .main-header {
        background: var(--primary-color);
        color: white;
        padding: 3rem 2rem;
        border-radius: var(--radius-xl);
        text-align: center;
        margin-bottom: 3rem;
        box-shadow: var(--shadow-lg);
        position: relative;
        overflow: hidden;
    }
    
    .main-header::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.1'%3E%3Ccircle cx='30' cy='30' r='2'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
        opacity: 0.3;
    }
    
    .main-header h1 {
        font-family: 'Inter', sans-serif;
        font-weight: 700;
        font-size: 2.5rem;
        margin: 0 0 0.5rem 0;
        position: relative;
        z-index: 1;
    }
    
    .main-header p {
        font-family: 'Inter', sans-serif;
        font-weight: 400;
        font-size: 1.1rem;
        margin: 0;
        opacity: 0.9;
        position: relative;
        z-index: 1;
    }
    
    /* Caja de solución moderna - Sin gradientes */
    .solution-box {
        background: #f0fdf4;
        border: 2px solid var(--success-color);
        border-radius: var(--radius-lg);
        padding: 2rem;
        margin: 2rem 0;
        text-align: center;
        box-shadow: var(--shadow-md);
        position: relative;
        overflow: hidden;
    }
    
    .solution-box::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 4px;
        background: var(--success-color);
    }
    
    .solution-box strong {
        font-family: 'Inter', sans-serif;
        font-weight: 600;
        font-size: 1.4rem;
        color: var(--text-primary);
        display: block;
        margin-bottom: 0.5rem;
    }
    
    /* Contenedor de pasos mejorado */
    .step-container {
        background: var(--background-card);
        border: 1px solid var(--border-color);
        border-left: 4px solid var(--primary-color);
        border-radius: var(--radius-md);
        padding: 1.5rem;
        margin: 1rem 0;
        box-shadow: var(--shadow-sm);
        font-family: 'Inter', sans-serif;
        color: var(--text-primary);
    }
    
    .step-container strong {
        color: var(--primary-color);
        font-weight: 600;
    }
    
    /* Display de ecuaciones */
    .equation-display {
        background: var(--background-light);
        border: 1px solid var(--border-color);
        border-radius: var(--radius-md);
        padding: 1rem;
        margin: 1rem 0;
        text-align: center;
        font-family: 'JetBrains Mono', monospace;
        font-size: 1.1rem;
        box-shadow: var(--shadow-sm);
    }
    
    .equation-display strong {
        font-family: 'Inter', sans-serif;
        color: var(--text-secondary);
        font-size: 0.9rem;
        text-transform: uppercase;
        letter-spacing: 0.05em;
    }
    
    /* Botones modernos */
    .stButton > button {
        font-family: 'Inter', sans-serif !important;
        font-weight: 500 !important;
        border-radius: var(--radius-md) !important;
        height: 3rem !important;
        border: none !important;
        transition: all 0.2s ease !important;
        box-shadow: var(--shadow-sm) !important;
    }
    
    .stButton > button:hover {
        transform: translateY(-1px) !important;
        box-shadow: var(--shadow-md) !important;
    }
    
    .stButton > button[kind="primary"] {
        background: var(--primary-color) !important;
        color: white !important;
    }
    
    .stButton > button[kind="secondary"] {
        background: var(--background-card) !important;
        border: 1px solid var(--border-color) !important;
        color: var(--text-primary) !important;
    }
    
    /* Input de texto moderno */
    .stTextInput > div > div > input {
        font-family: 'JetBrains Mono', monospace !important;
        font-size: 1.1rem !important;
        text-align: center !important;
        border-radius: var(--radius-md) !important;
        border: 2px solid var(--border-color) !important;
        padding: 0.75rem 1rem !important;
        transition: all 0.2s ease !important;
        box-shadow: var(--shadow-sm) !important;
    }
    
    .stTextInput > div > div > input:focus {
        border-color: var(--primary-color) !important;
        box-shadow: 0 0 0 3px rgb(99 102 241 / 0.1) !important;
    }
    
    /* Selectbox moderno */
    .stSelectbox > div > div > div {
        border-radius: var(--radius-md) !important;
        border: 2px solid var(--border-color) !important;
        font-family: 'Inter', sans-serif !important;
    }
    
    /* Sidebar moderno */
    .css-1d391kg {
        background: var(--background-light) !important;
        border-right: 1px solid var(--border-color) !important;
    }
    
    /* Botones del sidebar */
    .stButton > button[kind="secondary"] {
        width: 100% !important;
        text-align: left !important;
        justify-content: flex-start !important;
        font-size: 0.9rem !important;
        padding: 0.5rem 1rem !important;
        margin: 0.25rem 0 !important;
        background: var(--background-card) !important;
        border: 1px solid var(--border-color) !important;
        border-radius: var(--radius-sm) !important;
    }
    
    .stButton > button[kind="secondary"]:hover {
        background: var(--primary-color) !important;
        color: white !important;
        border-color: var(--primary-color) !important;
    }
    
    /* Alertas modernas */
    .stAlert {
        border-radius: var(--radius-md) !important;
        border: none !important;
        font-family: 'Inter', sans-serif !important;
        box-shadow: var(--shadow-sm) !important;
    }
    
    /* Expander moderno */
    .streamlit-expanderHeader {
        font-family: 'Inter', sans-serif !important;
        font-weight: 500 !important;
        background: var(--background-light) !important;
        border-radius: var(--radius-md) !important;
        border: 1px solid var(--border-color) !important;
    }
    
    /* Métricas y estadísticas */
    .metric-card {
        background: var(--background-card);
        border: 1px solid var(--border-color);
        border-radius: var(--radius-lg);
        padding: 1.5rem;
        text-align: center;
        box-shadow: var(--shadow-sm);
        transition: all 0.2s ease;
    }
    
    .metric-card p {
        color: var(--text-primary);
        margin: 0.5rem 0 0 0;
        font-weight: 600;
    }
    
    .metric-card:hover {
        box-shadow: var(--shadow-md);
        transform: translateY(-2px);
    }
    
    /* Animaciones suaves */
    * {
        transition: all 0.2s ease;
    }
    
    /* Responsive design */
    @media (max-width: 768px) {
        .main-header h1 {
            font-size: 2rem;
        }
        
        .main-header p {
            font-size: 1rem;
        }
        
        .solution-box {
            padding: 1.5rem;
        }
    }
    </style>
    """, unsafe_allow_html=True)

def main():
    # Configurar página con diseño moderno
    st.set_page_config(
        page_title="Examen Final Pro - Calculadora Avanzada",
        page_icon="",
        layout="wide",
        initial_sidebar_state="collapsed",
        menu_items={
            'Get Help': 'https://github.com/tu-usuario/mathsolver-pro',
            'Report a bug': 'https://github.com/tu-usuario/mathsolver-pro/issues',
            'About': "Exame Final Pro - Calculadora profesional de ecuaciones matemáticas"
        }
    )
    
    # Aplicar CSS personalizado
    apply_custom_css()
    
    # Header principal moderno
    st.markdown("""
    <div class="main-header">
        <h1>Examen Final Pro</h1>
        <p>Resuelve ecuaciones <strong>algebraicas</strong> y <strong>diferenciales</strong> con explicaciones paso a paso</p>
    </div>
    """, unsafe_allow_html=True)
    
    # Inicializar componentes
    parser = EquationParser()
    solver = LinearEquationSolver()
    diff_solver = DifferentialEquationSolver()
    
    # Selector de tipo de ecuación con diseño moderno
    st.markdown(" Configuración")
    
    col1, col2 = st.columns([2, 1])
    with col1:
        equation_type = st.selectbox(
            "Tipo de ecuación:",
            ["Algebraica (ax + b = c)", "Diferencial (dy/dx = f(x,y))"],
            help="Selecciona el tipo de ecuación que quieres resolver",
            label_visibility="collapsed"
        )
    
    # Selector de método para ecuaciones diferenciales
    selected_method = 'auto'
    if "Diferencial" in equation_type:
        available_methods = diff_solver.get_available_methods()
        method_options = {info['name']: key for key, info in available_methods.items()}
        method_names = list(method_options.keys())
        
        col1, col2 = st.columns([2, 1])
        with col1:
            selected_method_name = st.selectbox(
                "Método de resolución:",
                method_names,
                help="Selecciona el método específico para resolver la ecuación diferencial",
                index=0  # Por defecto "Automático"
            )
            selected_method = method_options[selected_method_name]
        
        with col2:
            # Mostrar descripción del método seleccionado
            if selected_method in available_methods:
                method_desc = available_methods[selected_method]['description']
                st.info(f"ℹ️ {method_desc}")
    

    
    # Campo de entrada principal con integración de ejemplos
    default_value = ""
    auto_solve = False
    
    if 'example_equation' in st.session_state:
        default_value = st.session_state.example_equation
        del st.session_state.example_equation
        if 'auto_solve' in st.session_state:
            auto_solve = st.session_state.auto_solve
            del st.session_state.auto_solve
    elif 'random_equation' in st.session_state:
        default_value = st.session_state.random_equation
        del st.session_state.random_equation
        if 'auto_solve' in st.session_state:
            auto_solve = st.session_state.auto_solve
            del st.session_state.auto_solve
    
    # Placeholder dinámico según el tipo
    if "Algebraica" in equation_type:
        placeholder = "Ejemplo: 2*x + 3 = 7"
        help_text = "Ecuación algebraica: usa * para multiplicación, variable 'x'"
    else:
        placeholder = "Ejemplo: dy/dx = 3*x**2"
        help_text = "Ecuación diferencial: usa dy/dx o y' para derivadas"
    
    # Campo de entrada con diseño moderno
    st.markdown("Entrada de Ecuación")
    
    equation_input = st.text_input(
        "Ecuación:",
        value=default_value,
        placeholder=placeholder,
        help=help_text,
        label_visibility="collapsed"
    )
    
    # Mostrar información contextual

    
    # Botones de acción modernos
    st.markdown("Acciones")
    
    col1, col2 = st.columns([3, 1])
    with col1:
        solve_button = st.button("🔍 Resolver Ecuación", type="primary", use_container_width=True)
    with col2:
        clear_button = st.button("🗑️ Limpiar", use_container_width=True)
    
    if clear_button:
        st.rerun()
    
    # Resolver automáticamente si viene de un ejemplo o si se presiona el botón
    should_solve = (solve_button and equation_input) or (auto_solve and equation_input)
    
    if should_solve:
        if "Algebraica" in equation_type:
            # Resolver ecuación algebraica
            validation = parser.validate_format(equation_input)
            
            if not validation['is_valid']:
                st.error("Error en el formato:")
                for error in validation['errors']:
                    st.error(f"• {error}")
                for suggestion in validation['suggestions']:
                    st.info(f"{suggestion}")
                return
            
            equation, is_valid, message = parser.parse(equation_input)
            
            if not is_valid:
                st.error(f"{message}")
                return
            
            if not solver.is_linear(equation):
                st.error("Esta no es una ecuación de primer grado")
                st.info("Las ecuaciones de primer grado tienen la forma ax + b = c, donde el exponente de x es 1")
                return
            
            result = solver.solve_with_steps(equation)
            
        else:
            # Resolver ecuación diferencial
            validation = diff_solver.validate_differential_format(equation_input)
            
            if not validation['is_valid']:
                st.error("Error en el formato:")
                for error in validation['errors']:
                    st.error(f"• {error}")
                for suggestion in validation['suggestions']:
                    st.info(f"{suggestion}")
                return
            
            equation, is_valid, message = diff_solver.parse_differential_equation(equation_input)
            
            if not is_valid:
                st.error(f"{message}")
                return
            
            result = diff_solver.solve_with_steps(equation, method=selected_method)
        
        # Mostrar resultado principal con diseño moderno
        st.markdown("---")
        st.markdown("## Resultado")
        
        if "Algebraica" in equation_type:
            # Mostrar resultados algebraicos
            if result['solution_type'] == 'unica_solucion':
                st.markdown(f"""
                <div class="solution-box">
                    <strong>Solución: x = {result['solution']}</strong>
                </div>
                """, unsafe_allow_html=True)
                
            elif result['solution_type'] == 'sin_solucion':
                st.warning("**Esta ecuación no tiene solución**")
                st.markdown("*No existe ningún valor de x que satisfaga la ecuación.*")
                
            elif result['solution_type'] == 'infinitas_soluciones':
                st.info("ℹ**Esta ecuación tiene infinitas soluciones**")
                st.markdown("*Cualquier valor de x satisface la ecuación.*")
                
            else:
                st.error(f"{result['explanation']}")
        
        else:
            # Mostrar resultados diferenciales
            if result['solution_type'] == 'solucion_general':
                st.markdown(f"""
                <div class="solution-box">
                    <strong>Solución General:</strong><br>
                    y(x) = {result['solution']}
                </div>
                """, unsafe_allow_html=True)
                
                # Mostrar tipo de ecuación y método usado
                col1, col2 = st.columns(2)
                with col1:
                    st.info(f"**Tipo:** {result['equation_type']}")
                with col2:
                    if 'method_used' in result:
                        st.info(f"**Método:** {result['method_used']}")
                
            else:
                st.error(f"{result['explanation']}")
        
        # Mostrar pasos de resolución con diseño moderno
        if result['steps']:
            st.markdown("---")
            st.markdown("## Proceso de Resolución")
            
            # Mostrar estadísticas del proceso
            col1, col2, col3 = st.columns(3)
            with col1:
                st.markdown("""
                <div class="metric-card">
                    <h3 style="color: #2563eb; margin: 0;"></h3>
                    <p style="margin: 0.5rem 0 0 0; font-weight: 600; color: #1e293b;">{} Pasos</p>
                </div>
                """.format(len(result['steps'])), unsafe_allow_html=True)
            
            with col2:
                st.markdown("""
                <div class="metric-card">
                    <h3 style="color: #059669; margin: 0;"></h3>
                    <p style="margin: 0.5rem 0 0 0; font-weight: 600; color: #1e293b;">Resuelto</p>
                </div>
                """, unsafe_allow_html=True)
            
            with col3:
                difficulty = "Básico" if len(result['steps']) <= 3 else "Intermedio" if len(result['steps']) <= 5 else "Avanzado"
                st.markdown(f"""
                <div class="metric-card">
                    <h3 style="color: #d97706; margin: 0;"></h3>
                    <p style="margin: 0.5rem 0 0 0; font-weight: 600; color: #1e293b;">{difficulty}</p>
                </div>
                """, unsafe_allow_html=True)
            
            st.markdown("<br>", unsafe_allow_html=True)
            
            for i, step in enumerate(result['steps']):
                with st.expander(f"Paso {step['step_number']}: {step['operation']}", 
                               expanded=(i < 2)):  # Solo expandir los primeros 2 pasos
                    
                    # Mostrar ecuación con formato mejorado
                    st.markdown(f"""
                    <div class="equation-display">
                        <strong>Ecuación:</strong>
                    </div>
                    """, unsafe_allow_html=True)
                    
                    # Usar LaTeX si está disponible, sino usar texto normal
                    if 'latex' in step and step['latex']:
                        st.latex(step['latex'])
                    else:
                        st.latex(step['equation'])
                    
                    # Mostrar explicación
                    st.markdown(f"""
                    <div class="step-container">
                        <strong>Explicación:</strong> {step['explanation']}
                    </div>
                    """, unsafe_allow_html=True)
        
        # Mostrar explicación final con mejor formato
        st.markdown("---")
        st.markdown(f"""
        <div style="background-color: #000000; padding: 1rem; border-radius: 10px; border-left: 4px solid #2196f3;">
            <strong>Resumen:</strong> {result['explanation']}
        </div>
        """, unsafe_allow_html=True)
    
    elif solve_button and not equation_input:
        st.error("Por favor ingresa una ecuación")
    
    # Mostrar instrucciones si no hay ecuación
    elif not equation_input and not auto_solve:
        st.info("Escribe tu ecuación para comenzar")
        


if __name__ == "__main__":
    main()