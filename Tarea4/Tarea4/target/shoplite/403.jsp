<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ShopLite - Acceso Denegado</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 100px auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background-color: white;
            padding: 40px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            text-align: center;
        }
        h1 {
            color: #dc3545;
            font-size: 48px;
            margin: 0;
        }
        h2 {
            color: #333;
            margin-top: 20px;
        }
        p {
            color: #666;
            margin: 20px 0;
        }
        .btn {
            display: inline-block;
            padding: 12px 24px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            margin-top: 20px;
        }
        .btn:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>403</h1>
        <h2>Acceso Denegado</h2>
        <p>No tienes permisos para acceder a esta página. Solo los administradores pueden acceder al panel de administración.</p>
        <a href="${pageContext.request.contextPath}/app/home" class="btn">Volver a Productos</a>
    </div>
</body>
</html>

