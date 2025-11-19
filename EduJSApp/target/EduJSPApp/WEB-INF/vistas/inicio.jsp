<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
    <h1>
        <i class="bi bi-mortarboard"></i> Bienvenido a EduJSPApp
    </h1>
    <p class="lead">Sistema de Gestión Educativa</p>
</div>

<div class="row">
    <div class="col-md-6 mb-4">
        <div class="card shadow-sm h-100">
            <div class="card-body">
                <h5 class="card-title">
                    <i class="bi bi-book text-primary"></i> Gestión de Carreras
                </h5>
                <p class="card-text">
                    Administre las carreras universitarias del sistema. Registre, edite y consulte información 
                    sobre las diferentes carreras ofrecidas.
                </p>
                <div class="d-grid gap-2">
                    <a href="${pageContext.request.contextPath}/carreras?accion=listar" 
                       class="btn btn-primary">
                        <i class="bi bi-list-ul"></i> Ver Carreras
                    </a>
                    <a href="${pageContext.request.contextPath}/carreras?accion=nuevo" 
                       class="btn btn-outline-primary">
                        <i class="bi bi-plus-circle"></i> Registrar Nueva Carrera
                    </a>
                </div>
            </div>
        </div>
    </div>
    
    

