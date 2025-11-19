<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="nav flex-column">
    <div class="text-center mb-4">
        <h4 class="text-white">
            <i class="bi bi-mortarboard"></i> EduJSPApp
        </h4>
    </div>
    
    <hr class="text-white">
    
    <h6 class="text-white-50 px-3 mb-3">MÓDULOS</h6>
    
    <!-- Enlaces de Carreras -->
    <a class="nav-link" href="${pageContext.request.contextPath}/carreras?accion=listar">
        <i class="bi bi-list-ul"></i> Listar Carreras
    </a>
    <a class="nav-link" href="${pageContext.request.contextPath}/carreras?accion=nuevo">
        <i class="bi bi-plus-circle"></i> Registrar Carrera
    </a>
    
    <hr class="text-white mt-4">
    
    <a class="nav-link" href="${pageContext.request.contextPath}/">
        <i class="bi bi-house"></i> Inicio
    </a>
</nav>

