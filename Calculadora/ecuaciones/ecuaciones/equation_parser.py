"""
Parser de ecuaciones lineales usando SymPy
"""

from sympy import symbols, Eq, sympify, parse_expr
from sympy.parsing.sympy_parser import parse_expr
from typing import Tuple, Optional, Dict, Any
import re

class EquationParser:
    def __init__(self):
        self.x = symbols('x')
    
    def parse(self, equation_str: str) -> Tuple[Optional[Eq], bool, str]:
        """
        Parsea una ecuación string usando SymPy
        
        Args:
            equation_str: String de la ecuación (ej: "2*x + 3 = 7")
            
        Returns:
            Tuple[ecuación_sympy, es_válida, mensaje_error]
        """
        try:
            # Limpiar la entrada
            equation_str = equation_str.strip()
            
            # Verificar que tenga exactamente un signo de igualdad
            if equation_str.count('=') != 1:
                if equation_str.count('=') == 0:
                    return None, False, "La ecuación debe contener un signo de igualdad (=)"
                else:
                    return None, False, "La ecuación debe contener exactamente un signo de igualdad"
            
            # Dividir por el signo de igualdad
            left_str, right_str = equation_str.split('=')
            left_str = left_str.strip()
            right_str = right_str.strip()
            
            # Verificar que ambos lados no estén vacíos
            if not left_str or not right_str:
                return None, False, "Ambos lados de la ecuación deben tener contenido"
            
            # Parsear cada lado usando SymPy
            left_expr = parse_expr(left_str, transformations='all')
            right_expr = parse_expr(right_str, transformations='all')
            
            # Crear la ecuación
            equation = Eq(left_expr, right_expr)
            
            # Verificar que contenga la variable x
            if not equation.has(self.x):
                return None, False, "La ecuación debe contener la variable 'x'"
            
            return equation, True, "Ecuación parseada correctamente"
            
        except Exception as e:
            return None, False, f"Error al parsear la ecuación: {str(e)}"
    
    def validate_format(self, equation_str: str) -> Dict[str, Any]:
        """
        Valida el formato de la ecuación antes del parsing
        
        Returns:
            Dict con información de validación
        """
        result = {
            'is_valid': True,
            'errors': [],
            'warnings': [],
            'suggestions': []
        }
        
        # Verificar caracteres permitidos
        allowed_chars = set('0123456789+-*/=x.() ')
        invalid_chars = set(equation_str) - allowed_chars
        
        if invalid_chars:
            result['is_valid'] = False
            result['errors'].append(f"Caracteres no permitidos: {', '.join(invalid_chars)}")
            result['suggestions'].append("Solo se permiten números, +, -, *, /, =, x, paréntesis y espacios")
        
        # Verificar que contenga x
        if 'x' not in equation_str.lower():
            result['warnings'].append("La ecuación debería contener la variable 'x'")
        
        # Verificar signos de igualdad
        equal_count = equation_str.count('=')
        if equal_count == 0:
            result['is_valid'] = False
            result['errors'].append("Falta el signo de igualdad (=)")
        elif equal_count > 1:
            result['is_valid'] = False
            result['errors'].append("Solo debe haber un signo de igualdad")
        
        return result