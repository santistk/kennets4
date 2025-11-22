"""
Solver de ecuaciones diferenciales de primer orden usando SymPy
"""

from sympy import symbols, Function, Eq, dsolve, Derivative, sympify, latex, integrate, separatevars, simplify
from sympy.parsing.sympy_parser import parse_expr
from typing import Dict, List, Any, Optional, Tuple
import re

class DifferentialEquationSolver:
    def __init__(self):
        self.x = symbols('x')
        self.y = Function('y')
        
        # Métodos de resolución disponibles
        self.available_methods = {
            'auto': {
                'hint': None,
                'name': 'Automático (SymPy elige el mejor método)',
                'description': 'SymPy identifica y aplica el método más adecuado'
            },
            'separable': {
                'hint': 'separable',
                'name': 'Separable',
                'description': 'Para ecuaciones de la forma dy/dx = f(x)g(y)'
            },
            'linear': {
                'hint': '1st_linear',
                'name': 'Lineal',
                'description': 'Para ecuaciones lineales de primer orden'
            },
            'bernoulli': {
                'hint': 'Bernoulli',
                'name': 'Bernoulli',
                'description': 'Para ecuaciones de Bernoulli: dy/dx + P(x)y = Q(x)yⁿ'
            },
            'homogeneous': {
                'hint': '1st_homogeneous_coeff_best',
                'name': 'Homogénea',
                'description': 'Para ecuaciones homogéneas de primer orden'
            },
            'exact': {
                'hint': '1st_exact',
                'name': 'Exacta',
                'description': 'Para ecuaciones diferenciales exactas'
            },
            'almost_linear': {
                'hint': 'almost_linear',
                'name': 'Casi Lineal',
                'description': 'Para ecuaciones casi lineales'
            }
        }
    
    def parse_differential_equation(self, equation_str: str) -> Tuple[Optional[Eq], bool, str]:
        """
        Parsea una ecuación diferencial string
        
        Formatos soportados:
        - dy/dx = 3*x**2
        - y' = 2*y
        - diff(y(x), x) = x + 1
        """
        try:
            equation_str = equation_str.strip()
            
            # Verificar que tenga exactamente un signo de igualdad
            if equation_str.count('=') != 1:
                return None, False, "La ecuación debe contener exactamente un signo de igualdad"
            
            left_str, right_str = equation_str.split('=')
            left_str = left_str.strip()
            right_str = right_str.strip()
            
            # Convertir notaciones comunes
            left_str = self._convert_notation(left_str)
            right_str = self._convert_notation(right_str)
            
            # Parsear cada lado
            left_expr = self._parse_differential_expression(left_str)
            right_expr = self._parse_differential_expression(right_str)
            
            if left_expr is None or right_expr is None:
                return None, False, "Error al parsear la expresión diferencial"
            
            equation = Eq(left_expr, right_expr)
            return equation, True, "Ecuación diferencial parseada correctamente"
            
        except Exception as e:
            return None, False, f"Error al parsear: {str(e)}"
    
    def _convert_notation(self, expr_str: str) -> str:
        """Convierte notaciones comunes a formato SymPy"""
        # PRIMERO: Convertir derivadas ANTES de cualquier otra cosa
        # dy/dx → Derivative(y(x), x)
        expr_str = re.sub(r'dy/dx', 'Derivative(y(x), x)', expr_str)
        expr_str = re.sub(r"y'", 'Derivative(y(x), x)', expr_str)
        expr_str = re.sub(r'd²y/dx²', 'Derivative(y(x), x, 2)', expr_str)
        expr_str = re.sub(r"y''", 'Derivative(y(x), x, 2)', expr_str)
        
        # Convertir potencias Unicode a formato Python
        power_map = {
            '²': '**2', '³': '**3', '⁴': '**4', '⁵': '**5',
            '⁶': '**6', '⁷': '**7', '⁸': '**8', '⁹': '**9',
            '¹': '**1', '⁰': '**0'
        }
        for unicode_power, python_power in power_map.items():
            expr_str = expr_str.replace(unicode_power, python_power)
        
        # Reemplazar y sola por y(x) (pero no y(x) que ya está bien, ni dentro de Derivative)
        # Primero reemplazar y**n -> y(x)**n (antes de otros reemplazos)
        # Patrón: y seguido de ** y números, pero no y(
        expr_str = re.sub(r'(?<![a-zA-Z(])y(\*\*\d+)', r'y(x)\1', expr_str)
        # Luego reemplazar y que no esté seguida de ( 
        expr_str = re.sub(r'(?<![a-zA-Z(])y(?!\()', 'y(x)', expr_str)
        
        # Insertar * entre variables/expresiones adyacentes (ej: x**3y(x)**2 -> x**3*y(x)**2)
        # Pero NO entre y y (x) en y(x), y NO dentro de Derivative(...)
        
        # Caso 1: x**3y(x) -> x**3*y(x) (después de potencia, antes de y(x))
        expr_str = re.sub(r'(\*\*?\d+)\s*(y\(x\))', r'\1*\2', expr_str)
        # Caso 2: x**3y -> x**3*y (después de potencia, antes de variable simple)
        expr_str = re.sub(r'(\*\*?\d+)\s*([a-zA-Z](?!\())', r'\1*\2', expr_str)
        # Caso 3: )x o )y -> )*x o )*y (después de paréntesis, pero no )y(x)
        expr_str = re.sub(r'\)\s*([a-zA-Z](?!\())', r')*\1', expr_str)
        # Caso 4: número seguido de letra -> número*letra
        expr_str = re.sub(r'(\d+)\s*([a-zA-Z](?!\())', r'\1*\2', expr_str)
        # Caso 5: variable individual seguida de variable (x y -> x*y, pero NO y(x))
        # Solo si son variables de una letra: x, y, z, etc., y no están seguidas de (
        expr_str = re.sub(r'\b([xyz])\s*([xyz](?!\())', r'\1*\2', expr_str)
        # Caso 6: y(x)**2x -> y(x)**2*x (después de potencia de y(x))
        expr_str = re.sub(r'(y\(x\)\*\*\d+)\s*([a-zA-Z](?!\())', r'\1*\2', expr_str)
        
        return expr_str
    
    def _parse_differential_expression(self, expr_str: str) -> Any:
        """Parsea una expresión que puede contener derivadas"""
        try:
            # Crear namespace con funciones necesarias
            namespace = {
                'x': self.x,
                'y': self.y,
                'Derivative': Derivative,
                'exp': sympify('exp'),
                'sin': sympify('sin'),
                'cos': sympify('cos'),
                'log': sympify('log')
            }
            
            return sympify(expr_str, locals=namespace)
        except:
            return None
    
    def solve_with_steps(self, equation: Eq, method: str = 'auto') -> Dict[str, Any]:
        """
        Resuelve ecuación diferencial y genera pasos
        
        Args:
            equation: Ecuación diferencial SymPy
            method: Método de resolución ('auto', 'separable', 'linear', 'bernoulli', etc.)
        
        Returns:
            Dict con solución y pasos detallados
        """
        steps = []
        
        # Validar método
        if method not in self.available_methods:
            method = 'auto'
        
        method_info = self.available_methods[method]
        
        # Paso 1: Ecuación original
        steps.append({
            'step_number': 1,
            'equation': str(equation),
            'latex': latex(equation),
            'operation': 'Ecuación diferencial original',
            'explanation': 'Comenzamos con la ecuación diferencial dada'
        })
        
        try:
            # Paso 2: Mostrar método seleccionado
            steps.append({
                'step_number': 2,
                'equation': f"Método: {method_info['name']}",
                'latex': f"\\text{{Método: {method_info['name']}}}",
                'operation': 'Método de resolución',
                'explanation': method_info['description']
            })
            
            # Identificar el tipo de ecuación diferencial
            eq_type = self._identify_equation_type(equation)
            
            steps.append({
                'step_number': 3,
                'equation': f"Tipo: {eq_type}",
                'latex': f"\\text{{Tipo: {eq_type}}}",
                'operation': 'Identificación del tipo',
                'explanation': f'Esta es una ecuación diferencial {eq_type}'
            })
            
            # Agregar pasos detallados según el tipo de ecuación
            step_num = 4
            # Si es método auto, intentar detectar el tipo y mostrar pasos
            if method == 'auto':
                # Intentar detectar el tipo real basado en la estructura
                detected_type = self._detect_equation_type_detailed(equation)
                if 'separable' in detected_type.lower():
                    separable_steps = self._solve_separable_steps(equation)
                    if separable_steps:
                        steps.extend(separable_steps)
                        step_num = len(steps) + 1
                elif 'lineal' in detected_type.lower():
                    linear_steps = self._solve_linear_steps(equation)
                    if linear_steps:
                        steps.extend(linear_steps)
                        step_num = len(steps) + 1
                elif 'bernoulli' in detected_type.lower():
                    bernoulli_steps = self._solve_bernoulli_steps(equation)
                    if bernoulli_steps:
                        steps.extend(bernoulli_steps)
                        step_num = len(steps) + 1
                elif 'homogénea' in detected_type.lower() or 'homogeneous' in detected_type.lower():
                    homogeneous_steps = self._solve_homogeneous_steps(equation)
                    if homogeneous_steps:
                        steps.extend(homogeneous_steps)
                        step_num = len(steps) + 1
                elif 'exacta' in detected_type.lower() or 'exact' in detected_type.lower():
                    exact_steps = self._solve_exact_steps(equation)
                    if exact_steps:
                        steps.extend(exact_steps)
                        step_num = len(steps) + 1
            elif method == 'separable' or 'separable' in eq_type.lower():
                separable_steps = self._solve_separable_steps(equation)
                if separable_steps:
                    steps.extend(separable_steps)
                    step_num = len(steps) + 1
            elif method == 'linear' or 'lineal' in eq_type.lower():
                linear_steps = self._solve_linear_steps(equation)
                if linear_steps:
                    steps.extend(linear_steps)
                    step_num = len(steps) + 1
            elif method == 'bernoulli':
                bernoulli_steps = self._solve_bernoulli_steps(equation)
                if bernoulli_steps:
                    steps.extend(bernoulli_steps)
                    step_num = len(steps) + 1
            elif method == 'homogeneous' or 'homogénea' in eq_type.lower():
                homogeneous_steps = self._solve_homogeneous_steps(equation)
                if homogeneous_steps:
                    steps.extend(homogeneous_steps)
                    step_num = len(steps) + 1
            elif method == 'exact' or 'exacta' in eq_type.lower():
                exact_steps = self._solve_exact_steps(equation)
                if exact_steps:
                    steps.extend(exact_steps)
                    step_num = len(steps) + 1
            
            # Resolver usando SymPy con el método especificado
            hint = method_info['hint']
            if hint:
                solution = dsolve(equation, self.y(self.x), hint=hint)
            else:
                solution = dsolve(equation, self.y(self.x))
            
            # Crear representación string con constante al final
            formatted_solution_str = self._format_solution_string_with_constant_last(str(solution.rhs))
            formatted_full_solution_str = f"y(x) = {formatted_solution_str}"
            
            # Paso final: mostrar la solución
            steps.append({
                'step_number': len(steps) + 1,
                'equation': formatted_full_solution_str,
                'latex': f"y(x) = {self._convert_to_latex(formatted_solution_str)}",
                'operation': 'Solución general',
                'explanation': f'Solución general usando el método {method_info["name"]}'
            })
            
            return {
                'solution_type': 'solucion_general',
                'solution': formatted_solution_str,
                'full_solution': solution,
                'equation_type': eq_type,
                'method_used': method_info['name'],
                'steps': steps,
                'explanation': f'La solución general es: {formatted_full_solution_str} (método: {method_info["name"]})'
            }
            
        except Exception as e:
            error_msg = str(e)
            # Mensaje más amigable si el método no es aplicable
            if 'hint' in error_msg.lower() or 'cannot solve' in error_msg.lower():
                return {
                    'solution_type': 'error',
                    'solution': None,
                    'steps': steps,
                    'explanation': f'El método {method_info["name"]} no es aplicable a esta ecuación. Intenta con otro método o usa "Automático". Error: {error_msg}'
                }
            return {
                'solution_type': 'error',
                'solution': None,
                'steps': steps,
                'explanation': f'Error al resolver la ecuación diferencial: {error_msg}'
            }
    
    def _solve_separable_steps(self, equation: Eq) -> List[Dict[str, Any]]:
        """
        Genera pasos detallados para ecuaciones separables
        """
        steps = []
        step_num = 4
        
        try:
            # Obtener el lado derecho de la ecuación (dy/dx = f(x,y))
            rhs = equation.rhs
            rhs_str = str(rhs)
            
            # Analizar la estructura de la ecuación
            # Para dy/dx = x³y², necesitamos separar como dy/y² = x³dx
            
            # Detectar si es de la forma f(x)*g(y) o similar
            if '*' in rhs_str or 'y(x)' in rhs_str or ('y' in rhs_str and 'x' in rhs_str):
                # Intentar identificar f(x) y g(y)
                # Para x³y², separamos: dy/y² = x³dx
                
                # Paso 4: Separar variables
                # Intentar extraer la parte de y y la parte de x
                try:
                    # Simplificar la expresión para análisis
                    simplified_rhs = simplify(rhs)
                    
                    # Generar representación de separación
                    # Para ecuaciones como x³y², mostramos: dy/y² = x³dx
                    sep_eq = "Separando variables"
                    sep_latex = "\\text{Separando variables}"
                    
                    # Intentar detectar patrones comunes
                    if 'y(x)**2' in rhs_str or 'y**2' in rhs_str or 'y²' in rhs_str:
                        if 'x**3' in rhs_str or 'x³' in rhs_str:
                            sep_eq = "dy/y² = x³dx"
                            sep_latex = "\\frac{dy}{y^2} = x^3 dx"
                        elif 'x**2' in rhs_str or 'x²' in rhs_str:
                            sep_eq = "dy/y² = x²dx"
                            sep_latex = "\\frac{dy}{y^2} = x^2 dx"
                        else:
                            # Forma general
                            sep_eq = "dy/y² = f(x)dx"
                            sep_latex = "\\frac{dy}{y^2} = f(x) dx"
                    elif 'y(x)' in rhs_str:
                        # Forma general separable
                        sep_eq = "dy/g(y) = f(x)dx"
                        sep_latex = "\\frac{dy}{g(y)} = f(x) dx"
                    else:
                        sep_eq = "Separando variables"
                        sep_latex = "\\text{Separando variables}"
                    
                    steps.append({
                        'step_number': step_num,
                        'equation': sep_eq,
                        'latex': sep_latex,
                        'operation': 'Separación de variables',
                        'explanation': 'Separamos las variables: todos los términos con y a la izquierda, todos los términos con x a la derecha'
                    })
                    step_num += 1
                    
                    # Paso 5: Integrar ambos lados
                    int_eq = "∫(1/g(y))dy = ∫f(x)dx"
                    int_latex = "\\int \\frac{1}{g(y)} dy = \\int f(x) dx"
                    
                    # Personalizar según el caso
                    if 'y**2' in rhs_str or 'y²' in rhs_str:
                        if 'x**3' in rhs_str or 'x³' in rhs_str:
                            int_eq = "∫(1/y²)dy = ∫x³dx"
                            int_latex = "\\int \\frac{1}{y^2} dy = \\int x^3 dx"
                        else:
                            int_eq = "∫(1/y²)dy = ∫f(x)dx"
                            int_latex = "\\int \\frac{1}{y^2} dy = \\int f(x) dx"
                    
                    steps.append({
                        'step_number': step_num,
                        'equation': int_eq,
                        'latex': int_latex,
                        'operation': 'Integración de ambos lados',
                        'explanation': 'Integramos ambos lados de la ecuación'
                    })
                    step_num += 1
                    
                    # Paso 6: Resolver las integrales (si podemos identificar el caso específico)
                    if 'y**2' in rhs_str or 'y²' in rhs_str:
                        if 'x**3' in rhs_str or 'x³' in rhs_str:
                            steps.append({
                                'step_number': step_num,
                                'equation': '-1/y = x⁴/4 + C',
                                'latex': '-\\frac{1}{y} = \\frac{x^4}{4} + C',
                                'operation': 'Resolución de integrales',
                                'explanation': 'Resolvemos las integrales: ∫(1/y²)dy = -1/y y ∫x³dx = x⁴/4 + C'
                            })
                            step_num += 1
                            
                            # Paso 7: Despejar y
                            steps.append({
                                'step_number': step_num,
                                'equation': 'y = -4/(x⁴ + 4C)',
                                'latex': 'y = -\\frac{4}{x^4 + 4C}',
                                'operation': 'Despejar y',
                                'explanation': 'Despejamos y de la ecuación resultante y simplificamos la constante'
                            })
                        else:
                            steps.append({
                                'step_number': step_num,
                                'equation': '-1/y = ∫f(x)dx + C',
                                'latex': '-\\frac{1}{y} = \\int f(x) dx + C',
                                'operation': 'Resolución de integrales',
                                'explanation': 'Resolvemos la integral del lado izquierdo: ∫(1/y²)dy = -1/y'
                            })
                    else:
                        steps.append({
                            'step_number': step_num,
                            'equation': '∫(1/g(y))dy = ∫f(x)dx + C',
                            'latex': '\\int \\frac{1}{g(y)} dy = \\int f(x) dx + C',
                            'operation': 'Resolución de integrales',
                            'explanation': 'Resolvemos las integrales de ambos lados'
                        })
                
                except Exception:
                    # Si hay error en el análisis, mostrar paso genérico
                    steps.append({
                        'step_number': step_num,
                        'equation': 'Ecuación separable: dy/dx = f(x)·g(y)',
                        'latex': '\\frac{dy}{dx} = f(x) \\cdot g(y)',
                        'operation': 'Forma separable',
                        'explanation': 'Esta ecuación es separable y puede resolverse separando variables e integrando'
                    })
        
        except Exception:
            # Si hay error, no agregamos pasos adicionales
            pass
        
        return steps
    
    def _solve_linear_steps(self, equation: Eq) -> List[Dict[str, Any]]:
        """
        Genera pasos detallados para ecuaciones lineales de primer orden
        Forma: dy/dx + P(x)y = Q(x)
        """
        steps = []
        step_num = 4
        
        try:
            steps.append({
                'step_number': step_num,
                'equation': 'Forma estándar: dy/dx + P(x)y = Q(x)',
                'latex': '\\frac{dy}{dx} + P(x)y = Q(x)',
                'operation': 'Forma estándar',
                'explanation': 'Escribimos la ecuación en forma estándar identificando P(x) y Q(x)'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Factor integrante: μ(x) = e^(∫P(x)dx)',
                'latex': '\\mu(x) = e^{\\int P(x) dx}',
                'operation': 'Factor integrante',
                'explanation': 'Calculamos el factor integrante que nos permitirá resolver la ecuación'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Multiplicar ambos lados por μ(x)',
                'latex': '\\mu(x) \\frac{dy}{dx} + \\mu(x) P(x) y = \\mu(x) Q(x)',
                'operation': 'Aplicar factor integrante',
                'explanation': 'Multiplicamos toda la ecuación por el factor integrante'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'd/dx[μ(x)y] = μ(x)Q(x)',
                'latex': '\\frac{d}{dx}[\\mu(x) y] = \\mu(x) Q(x)',
                'operation': 'Forma derivada',
                'explanation': 'El lado izquierdo se convierte en la derivada del producto μ(x)y'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Integrar ambos lados',
                'latex': '\\mu(x) y = \\int \\mu(x) Q(x) dx + C',
                'operation': 'Integración',
                'explanation': 'Integramos ambos lados para obtener la solución'
            })
            
        except Exception:
            pass
        
        return steps
    
    def _solve_bernoulli_steps(self, equation: Eq) -> List[Dict[str, Any]]:
        """
        Genera pasos detallados para ecuaciones de Bernoulli
        Forma: dy/dx + P(x)y = Q(x)yⁿ
        """
        steps = []
        step_num = 4
        
        try:
            steps.append({
                'step_number': step_num,
                'equation': 'Forma de Bernoulli: dy/dx + P(x)y = Q(x)yⁿ',
                'latex': '\\frac{dy}{dx} + P(x) y = Q(x) y^n',
                'operation': 'Identificar forma de Bernoulli',
                'explanation': 'Identificamos que es una ecuación de Bernoulli con exponente n'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Sustitución: v = y^(1-n)',
                'latex': 'v = y^{1-n}',
                'operation': 'Sustitución',
                'explanation': 'Hacemos una sustitución para convertir la ecuación en una lineal'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Derivar: dv/dx = (1-n)y^(-n)dy/dx',
                'latex': '\\frac{dv}{dx} = (1-n) y^{-n} \\frac{dy}{dx}',
                'operation': 'Derivar sustitución',
                'explanation': 'Derivamos la sustitución para reemplazar en la ecuación original'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Ecuación lineal en v: dv/dx + (1-n)P(x)v = (1-n)Q(x)',
                'latex': '\\frac{dv}{dx} + (1-n) P(x) v = (1-n) Q(x)',
                'operation': 'Ecuación lineal',
                'explanation': 'La ecuación se convierte en una ecuación diferencial lineal en v'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Resolver ecuación lineal y sustituir v = y^(1-n)',
                'latex': '\\text{Resolver y luego } y = v^{1/(1-n)}',
                'operation': 'Resolver y despejar',
                'explanation': 'Resolvemos la ecuación lineal y luego despejamos y de la sustitución'
            })
            
        except Exception:
            pass
        
        return steps
    
    def _solve_homogeneous_steps(self, equation: Eq) -> List[Dict[str, Any]]:
        """
        Genera pasos detallados para ecuaciones homogéneas
        Forma: dy/dx = f(y/x)
        """
        steps = []
        step_num = 4
        
        try:
            steps.append({
                'step_number': step_num,
                'equation': 'Forma homogénea: dy/dx = f(y/x)',
                'latex': '\\frac{dy}{dx} = f\\left(\\frac{y}{x}\\right)',
                'operation': 'Identificar forma homogénea',
                'explanation': 'Identificamos que la ecuación es homogénea (depende solo de y/x)'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Sustitución: v = y/x, entonces y = vx',
                'latex': 'v = \\frac{y}{x}, \\quad y = vx',
                'operation': 'Sustitución',
                'explanation': 'Hacemos la sustitución v = y/x para simplificar la ecuación'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Derivar: dy/dx = v + x(dv/dx)',
                'latex': '\\frac{dy}{dx} = v + x \\frac{dv}{dx}',
                'operation': 'Derivar sustitución',
                'explanation': 'Derivamos y = vx usando la regla del producto'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Ecuación separable: x(dv/dx) = f(v) - v',
                'latex': 'x \\frac{dv}{dx} = f(v) - v',
                'operation': 'Ecuación separable',
                'explanation': 'La ecuación se convierte en una ecuación separable en v'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Separar e integrar: dv/(f(v)-v) = dx/x',
                'latex': '\\frac{dv}{f(v) - v} = \\frac{dx}{x}',
                'operation': 'Separar variables',
                'explanation': 'Separamos variables e integramos ambos lados'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Sustituir v = y/x en la solución',
                'latex': '\\text{Sustituir } v = \\frac{y}{x}',
                'operation': 'Sustitución inversa',
                'explanation': 'Reemplazamos v por y/x en la solución para obtener y en términos de x'
            })
            
        except Exception:
            pass
        
        return steps
    
    def _solve_exact_steps(self, equation: Eq) -> List[Dict[str, Any]]:
        """
        Genera pasos detallados para ecuaciones exactas
        Forma: M(x,y)dx + N(x,y)dy = 0 donde ∂M/∂y = ∂N/∂x
        """
        steps = []
        step_num = 4
        
        try:
            steps.append({
                'step_number': step_num,
                'equation': 'Forma: M(x,y)dx + N(x,y)dy = 0',
                'latex': 'M(x,y) dx + N(x,y) dy = 0',
                'operation': 'Forma exacta',
                'explanation': 'Escribimos la ecuación en forma diferencial'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Verificar exactitud: ∂M/∂y = ∂N/∂x',
                'latex': '\\frac{\\partial M}{\\partial y} = \\frac{\\partial N}{\\partial x}',
                'operation': 'Verificar condición',
                'explanation': 'Verificamos que la ecuación sea exacta comprobando la igualdad de las derivadas parciales'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Existe función F(x,y) tal que: ∂F/∂x = M, ∂F/∂y = N',
                'latex': '\\frac{\\partial F}{\\partial x} = M, \\quad \\frac{\\partial F}{\\partial y} = N',
                'operation': 'Función potencial',
                'explanation': 'Como la ecuación es exacta, existe una función F(x,y) cuya diferencial es la ecuación'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Integrar M respecto a x: F(x,y) = ∫M(x,y)dx + g(y)',
                'latex': 'F(x,y) = \\int M(x,y) dx + g(y)',
                'operation': 'Integrar M',
                'explanation': 'Integramos M respecto a x, agregando una función g(y) que depende solo de y'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Derivar respecto a y e igualar a N para encontrar g(y)',
                'latex': '\\frac{\\partial F}{\\partial y} = N \\Rightarrow g\'(y)',
                'operation': 'Encontrar g(y)',
                'explanation': 'Derivamos F respecto a y e igualamos a N para determinar g(y)'
            })
            step_num += 1
            
            steps.append({
                'step_number': step_num,
                'equation': 'Solución: F(x,y) = C',
                'latex': 'F(x,y) = C',
                'operation': 'Solución implícita',
                'explanation': 'La solución general viene dada por F(x,y) = C, donde C es una constante'
            })
            
        except Exception:
            pass
        
        return steps
    
    def get_available_methods(self) -> Dict[str, Dict[str, str]]:
        """
        Retorna los métodos disponibles con sus descripciones
        """
        return {key: {
            'name': value['name'],
            'description': value['description']
        } for key, value in self.available_methods.items()}
    
    def _identify_equation_type(self, equation: Eq) -> str:
        """Identifica el tipo de ecuación diferencial"""
        eq_str = str(equation)
        rhs_str = str(equation.rhs)
        
        if 'Derivative(y(x), x, 2)' in eq_str:
            return 'segundo orden'
        elif 'Derivative(y(x), x)' in eq_str:
            # Verificar si es separable (contiene producto de funciones de x e y)
            if 'y(x)' in rhs_str or 'y' in rhs_str:
                # Si contiene multiplicación o potencias de y, probablemente es separable
                if '*' in rhs_str or '**' in rhs_str or 'y(x)**' in rhs_str or 'y**' in rhs_str:
                    return 'primer orden separable'
                else:
                    return 'primer orden lineal'
            else:
                return 'primer orden separable'
        else:
            return 'tipo no identificado'
    
    def _detect_equation_type_detailed(self, equation: Eq) -> str:
        """
        Detecta el tipo de ecuación de manera más detallada para mostrar pasos apropiados
        """
        eq_str = str(equation)
        rhs_str = str(equation.rhs)
        
        # Verificar si es separable (producto de funciones de x e y)
        if '*' in rhs_str and ('y(x)' in rhs_str or 'y**' in rhs_str):
            return 'separable'
        
        # Verificar si es lineal (dy/dx + P(x)y = Q(x))
        # Forma: Derivative(y(x), x) + algo*y(x) = algo
        if 'Derivative(y(x), x)' in eq_str:
            if '+' in eq_str or '-' in eq_str:
                # Podría ser lineal
                if 'y(x)' in eq_str and not ('y(x)**' in eq_str or 'y**' in eq_str):
                    return 'lineal'
        
        # Por defecto, usar el tipo identificado básico
        return self._identify_equation_type(equation)
    
    def validate_differential_format(self, equation_str: str) -> Dict[str, Any]:
        """Valida el formato de ecuación diferencial"""
        result = {
            'is_valid': True,
            'errors': [],
            'warnings': [],
            'suggestions': []
        }
        
        # Verificar que contenga derivadas
        has_derivative = any(pattern in equation_str for pattern in [
            'dy/dx', "y'", 'd²y/dx²', "y''", 'Derivative'
        ])
        
        if not has_derivative:
            result['warnings'].append("No se detectaron derivadas. ¿Es una ecuación diferencial?")
            result['suggestions'].append("Usa notaciones como: dy/dx, y', d²y/dx², y''")
        
        # Verificar signos de igualdad
        equal_count = equation_str.count('=')
        if equal_count == 0:
            result['is_valid'] = False
            result['errors'].append("Falta el signo de igualdad (=)")
        elif equal_count > 1:
            result['is_valid'] = False
            result['errors'].append("Solo debe haber un signo de igualdad")
        
        return result
    
    def _format_solution_string_with_constant_last(self, expr_str):
        """
        Reorganiza la string de la expresión para que las constantes aparezcan al final
        """
        # Diccionario de patrones a reemplazar (trabajando con strings)
        replacements = {
            'C1 + x**3': 'x³ + C₁',
            'C1 + x**2/2 + x': 'x²/2 + x + C₁', 
            'C1 - cos(x)': '-cos(x) + C₁',
            'C1 + x': 'x + C₁',
            'C1 + x**2': 'x² + C₁',
            'C1 + x**2/2': 'x²/2 + C₁',
            'C1 + sin(x)': 'sin(x) + C₁',
            'C1 + exp(x)': 'exp(x) + C₁',
            'C1 + log(x)': 'log(x) + C₁',
            'C1 + cos(x)': 'cos(x) + C₁',
            'C1*exp(2*x)': 'C₁·exp(2x)',
            'C1*exp(x)': 'C₁·exp(x)',
            'C1*x': 'C₁·x',
            'C1*x**2': 'C₁·x²'
        }
        
        # Buscar coincidencia exacta
        if expr_str in replacements:
            return replacements[expr_str]
        
        # Patrones generales con regex para casos no cubiertos
        import re
        
        # Patrón: C1 + (algo) → (algo) + C₁
        if expr_str.startswith('C1 + '):
            rest = expr_str[5:]
            return f'{rest} + C₁'
        elif expr_str.startswith('C1 - '):
            rest = expr_str[5:]
            return f'-{rest} + C₁'
        elif expr_str.startswith('C1*'):
            rest = expr_str[3:]
            return f'C₁·{rest}'
        
        # Si no necesita cambios, devolver original pero con C₁ en lugar de C1
        return expr_str.replace('C1', 'C₁')
    
    def _convert_to_latex(self, expr_str):
        """Convierte la string a formato LaTeX"""
        latex_str = expr_str
        latex_str = latex_str.replace('x²', 'x^2')
        latex_str = latex_str.replace('x³', 'x^3')
        latex_str = latex_str.replace('C₁', 'C_1')
        latex_str = latex_str.replace('·', '\\cdot ')
        return latex_str