<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="page-header">
    <div class="d-flex justify-content-between align-items-center">
        <h2>
            <i class="bi bi-list-ul"></i> Listado de Carreras Universitarias
        </h2>
        <a href="${pageContext.request.contextPath}/carreras?accion=nuevo" 
           class="btn btn-primary">
            <i class="bi bi-plus-circle"></i> Nueva Carrera
        </a>
    </div>
</div>

<c:choose>
    <c:when test="${empty carreras}">
        <div class="alert alert-info" role="alert">
            <i class="bi bi-info-circle"></i> No hay carreras registradas. 
            <a href="${pageContext.request.contextPath}/carreras?accion=nuevo" class="alert-link">
                Registrar la primera carrera
            </a>
        </div>
    </c:when>
    <c:otherwise>
        <div class="card shadow-sm">
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-hover table-striped">
                        <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Código</th>
                                <th>Facultad</th>
                                <th>Nivel</th>
                                <th>Modalidades</th>
                                <th>Estado</th>
                                <th>Descripción</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="carrera" items="${carreras}">
                                <tr>
                                    <td><strong>${carrera.id}</strong></td>
                                    <td>
                                        <i class="bi bi-book"></i> 
                                        <strong><c:out value="${carrera.nombre}"/></strong>
                                    </td>
                                    <td>
                                        <span class="badge bg-secondary">
                                            <c:out value="${carrera.codigo}"/>
                                        </span>
                                    </td>
                                    <td>
                                        <i class="bi bi-building"></i> 
                                        <c:out value="${carrera.facultad}"/>
                                    </td>
                                    <td>
                                        <span class="badge bg-info">
                                            <c:out value="${carrera.nivel}"/>
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty carrera.modalidades}">
                                                <c:forEach var="modalidad" items="${carrera.modalidades}" varStatus="status">
                                                    <span class="badge bg-primary me-1">
                                                        <c:out value="${modalidad}"/>
                                                    </span>
                                                    <c:if test="${!status.last}"> </c:if>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">Sin modalidades</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${carrera.estadoActiva}">
                                                <span class="badge bg-success">
                                                    <i class="bi bi-check-circle"></i> Activa
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger">
                                                    <i class="bi bi-x-circle"></i> Suspendida
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty carrera.descripcion}">
                                                <c:set var="descripcion" value="${carrera.descripcion}"/>
                                                <c:choose>
                                                    <c:when test="${fn:length(descripcion) > 50}">
                                                        <c:out value="${fn:substring(descripcion, 0, 50)}..."/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:out value="${descripcion}"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">Sin descripción</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="btn-group" role="group">
                                            <a href="${pageContext.request.contextPath}/carreras?accion=editar&id=${carrera.id}" 
                                               class="btn btn-sm btn-warning" 
                                               title="Editar">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/carreras?accion=eliminar&id=${carrera.id}" 
                                               class="btn btn-sm btn-danger" 
                                               title="Eliminar"
                                               onclick="return confirm('¿Está seguro de eliminar la carrera ${carrera.nombre}?');">
                                                <i class="bi bi-trash"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <div class="mt-3">
                    <p class="text-muted">
                        <i class="bi bi-info-circle"></i> 
                        Total de carreras registradas: <strong>${fn:length(carreras)}</strong>
                    </p>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>

