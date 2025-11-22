"""
Solver de ecuaciones lineales con generación de pasos
"""

from sympy import symbols, Eq, solve, simplify, expand, collect
from sympy.solvers.solvers import solve_linear
from typing import Dict, List, Any, Union
import sympy as sp

class LinearEquationSolver:
    def __init__(self):
        self.x = symbols('x')
    
    def solve_with_steps(self, equation: Eq) -> Dict[str, Any]:
        """
        Resuelve ecuación lineal y genera pasos detallados
        
        Args:
            equation: Ecuación SymPy de la forma Eq(left, right)
            
        Returns:
            Dict con solución y pasos detallados
        """
        steps = []
        
        # Paso 1: Ecuación original
        steps.append({
            'step_number': 1,
            'equation': str(equation),
            'operation': 'Ecuación original',
            'explanation': 'Comenzamos con la ecuación dada'
        })
        
        try:
            # Obtener los lados de la ecuación
            left_side = equation.lhs
            right_side = equation.rhs
            
            # Paso 2: Mover todos los términos al lado izquierdo
            combined = left_side - right_side
            combined = expand(combined)
            
            steps.append({
                'step_number': 2,
                'equation': f"{combined} = 0",
                'operation': 'Mover todos los términos al lado izquierdo',
                'explanation': 'Restamos el lado derecho de ambos lados'
            })
            
            # Paso 3: Simplificar y agrupar términos
            simplified = collect(combined, self.x)
            
            if simplified != combined:
                steps.append({
                    'step_number': 3,
                    'equation': f"{simplified} = 0",
                    'operation': 'Simplificar y agrupar términos',
                    'explanation': 'Agrupamos los términos con x y las constantes'
                })
            
            # Resolver usando SymPy
            solutions = solve(equation, self.x)
            
            # Analizar el tipo de solución
            if len(solutions) == 0:
                # Verificar si es inconsistente o identidad
                if combined.equals(0):
                    return {
                        'solution_type': 'infinitas_soluciones',
                        'solution': 'Infinitas soluciones',
                        'steps': steps,
                        'explanation': 'La ecuación es una identidad (0 = 0), por lo que cualquier valor de x es solución'
                    }
                else:
                    return {
                        'solution_type': 'sin_solucion',
                        'solution': 'Sin solución',
                        'steps': steps,
                        'explanation': 'La ecuación es inconsistente (por ejemplo: 5 = 0)'
                    }
            
            elif len(solutions) == 1:
                solution = solutions[0]
                
                # Paso final: mostrar la solución
                steps.append({
                    'step_number': len(steps) + 1,
                    'equation': f"x = {solution}",
                    'operation': 'Solución final',
                    'explanation': f'Por lo tanto, x = {solution}'
                })
                
                return {
                    'solution_type': 'unica_solucion',
                    'solution': solution,
                    'steps': steps,
                    'explanation': f'La ecuación tiene una única solución: x = {solution}'
                }
            
            else:
                # Múltiples soluciones (no debería ocurrir para ecuaciones lineales)
                return {
                    'solution_type': 'multiple_soluciones',
                    'solution': solutions,
                    'steps': steps,
                    'explanation': 'La ecuación tiene múltiples soluciones'
                }
                
        except Exception as e:
            return {
                'solution_type': 'error',
                'solution': None,
                'steps': steps,
                'explanation': f'Error al resolver la ecuación: {str(e)}'
            }
    
    def is_linear(self, equation: Eq) -> bool:
        """
        Verifica si la ecuación es realmente lineal (primer grado)
        """
        try:
            # Obtener el polinomio combinado
            combined = equation.lhs - equation.rhs
            expanded = expand(combined)
            
            # Verificar el grado del polinomio respecto a x
            degree = sp.degree(expanded, self.x)
            
            return degree <= 1
            
        except:
            return False
    
    def get_coefficients(self, equation: Eq) -> Dict[str, float]:
        """
        Extrae los coeficientes de la ecuación lineal ax + b = 0
        """
        try:
            combined = equation.lhs - equation.rhs
            expanded = expand(combined)
            
            # Extraer coeficiente de x y término independiente
            coeff_x = expanded.coeff(self.x, 1) or 0
            coeff_const = expanded.coeff(self.x, 0) or 0
            
            return {
                'a': float(coeff_x),
                'b': float(coeff_const)
            }
            
        except:
            return {'a': 0, 'b': 0}