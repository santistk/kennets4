<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ShopLite - Administración</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .header {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            margin: 0;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
            border: 1px solid #f5c6cb;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: bold;
        }
        input[type="text"],
        input[type="number"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .btn {
            padding: 12px 24px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin-right: 10px;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .btn-secondary {
            background-color: #6c757d;
        }
        .btn-secondary:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1>Panel de Administración</h1>
        <div>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary" style="text-decoration: none; display: inline-block;">Volver a Productos</a>
            <a href="${pageContext.request.contextPath}/auth/logout" class="btn" style="text-decoration: none; display: inline-block; background-color: #dc3545;">Cerrar Sesión</a>
        </div>
    </div>

    <div class="container">
        <h2>Crear Nuevo Producto</h2>
        
        <% if (request.getParameter("err") != null) { %>
            <div class="error">
                <strong>Error:</strong> Por favor, verifica que el nombre no esté vacío y el precio sea mayor a 0.
            </div>
        <% } %>
        
        <form action="${pageContext.request.contextPath}/admin" method="post">
            <div class="form-group">
                <label for="name">Nombre del Producto:</label>
                <input type="text" id="name" name="name" required>
            </div>
            
            <div class="form-group">
                <label for="price">Precio:</label>
                <input type="number" id="price" name="price" step="0.01" min="0.01" required>
            </div>
            
            <button type="submit" class="btn">Crear Producto</button>
        </form>
    </div>
</body>
</html>

