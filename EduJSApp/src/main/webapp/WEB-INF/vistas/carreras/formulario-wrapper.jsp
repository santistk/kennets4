<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="esEdicion" value="${carrera != null && carrera.id > 0}"/>
<c:set var="pageTitle" value="${esEdicion ? 'Editar Carrera' : 'Registrar Nueva Carrera'}" scope="request"/>
<c:set var="contentPage" value="/WEB-INF/vistas/carreras/formulario.jsp" scope="request"/>

<jsp:include page="/WEB-INF/templates/base.jsp"/>

