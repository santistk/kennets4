<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="esEdicion" value="${carrera != null && carrera.id > 0}"/>

<div class="page-header">
    <h2>
        <i class="bi bi-${esEdicion ? 'pencil' : 'plus-circle'}"></i> 
        ${esEdicion ? 'Editar Carrera' : 'Registrar Nueva Carrera'}
    </h2>
</div>

<div class="card shadow-sm">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/carreras" method="post" id="formCarrera">
            <input type="hidden" name="accion" value="${esEdicion ? 'actualizar' : 'guardar'}">
            <c:if test="${esEdicion}">
                <input type="hidden" name="id" value="${carrera.id}">
            </c:if>

            <div class="row mb-3">
                <!-- Campo: Nombre (Texto) -->
                <div class="col-md-6">
                    <label for="nombre" class="form-label">
                        <i class="bi bi-book"></i> Nombre de la Carrera <span class="text-danger">*</span>
                    </label>
                    <input type="text" 
                           class="form-control" 
                           id="nombre" 
                           name="nombre" 
                           value="<c:out value='${carrera.nombre}'/>"
                           required 
                           placeholder="Ej: Ingeniería en Sistemas">
                </div>

                <!-- Campo: Código (Texto) -->
                <div class="col-md-6">
                    <label for="codigo" class="form-label">
                        <i class="bi bi-hash"></i> Código <span class="text-danger">*</span>
                    </label>
                    <input type="text" 
                           class="form-control" 
                           id="codigo" 
                           name="codigo" 
                           value="<c:out value='${carrera.codigo}'/>"
                           required 
                           placeholder="Ej: SIS01, ARQ02"
                           pattern="[A-Z]{3}[0-9]{2}"
                           title="Formato: 3 letras mayúsculas seguidas de 2 números">
                </div>
            </div>

            <div class="row mb-3">
                <!-- Campo: Facultad (Select) -->
                <div class="col-md-6">
                    <label for="facultad" class="form-label">
                        <i class="bi bi-building"></i> Facultad <span class="text-danger">*</span>
                    </label>
                    <select class="form-select" id="facultad" name="facultad" required>
                        <option value="">-- Seleccione una Facultad --</option>
                        <option value="Ingeniería" ${carrera.facultad == 'Ingeniería' ? 'selected' : ''}>Ingeniería</option>
                        <option value="Arquitectura y Diseño" ${carrera.facultad == 'Arquitectura y Diseño' ? 'selected' : ''}>Arquitectura y Diseño</option>
                        <option value="Ciencias Económicas" ${carrera.facultad == 'Ciencias Económicas' ? 'selected' : ''}>Ciencias Económicas</option>
                        <option value="Ciencias de la Salud" ${carrera.facultad == 'Ciencias de la Salud' ? 'selected' : ''}>Ciencias de la Salud</option>
                        <option value="Humanidades" ${carrera.facultad == 'Humanidades' ? 'selected' : ''}>Humanidades</option>
                        <option value="Ciencias Jurídicas" ${carrera.facultad == 'Ciencias Jurídicas' ? 'selected' : ''}>Ciencias Jurídicas</option>
                    </select>
                </div>

                <!-- Campo: Nivel (Select) -->
                <div class="col-md-6">
                    <label for="nivel" class="form-label">
                        <i class="bi bi-award"></i> Nivel Académico <span class="text-danger">*</span>
                    </label>
                    <select class="form-select" id="nivel" name="nivel" required>
                        <option value="">-- Seleccione un Nivel --</option>
                        <option value="Técnico" ${carrera.nivel == 'Técnico' ? 'selected' : ''}>Técnico</option>
                        <option value="Licenciatura" ${carrera.nivel == 'Licenciatura' ? 'selected' : ''}>Licenciatura</option>
                        <option value="Maestría" ${carrera.nivel == 'Maestría' ? 'selected' : ''}>Maestría</option>
                        <option value="Doctorado" ${carrera.nivel == 'Doctorado' ? 'selected' : ''}>Doctorado</option>
                    </select>
                </div>
            </div>

            <div class="row mb-3">
                <!-- Campo: Modalidades (Checkboxes) -->
                <div class="col-md-6">
                    <label class="form-label">
                        <i class="bi bi-check2-square"></i> Modalidades Ofrecidas <span class="text-danger">*</span>
                    </label>
                    <div class="form-check">
                        <input class="form-check-input" 
                               type="checkbox" 
                               name="modalidades" 
                               value="Presencial" 
                               id="modalidadPresencial"
                               <c:if test="${carrera != null && carrera.modalidades.contains('Presencial')}">checked</c:if>>
                        <label class="form-check-label" for="modalidadPresencial">
                            Presencial
                        </label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" 
                               type="checkbox" 
                               name="modalidades" 
                               value="Virtual" 
                               id="modalidadVirtual"
                               <c:if test="${carrera != null && carrera.modalidades.contains('Virtual')}">checked</c:if>>
                        <label class="form-check-label" for="modalidadVirtual">
                            Virtual
                        </label>
                    </div>
                    <div class="form-check">
                        <input class="form-check-input" 
                               type="checkbox" 
                               name="modalidades" 
                               value="Semipresencial" 
                               id="modalidadSemipresencial"
                               <c:if test="${carrera != null && carrera.modalidades.contains('Semipresencial')}">checked</c:if>>
                        <label class="form-check-label" for="modalidadSemipresencial">
                            Semipresencial
                        </label>
                    </div>
                </div>

                <!-- Campo: Estado Activa (Checkbox) -->
                <div class="col-md-6">
                    <label class="form-label">
                        <i class="bi bi-toggle-on"></i> Estado
                    </label>
                    <div class="form-check form-switch">
                        <input class="form-check-input" 
                               type="checkbox" 
                               name="estadoActiva" 
                               id="estadoActiva"
                               <c:if test="${carrera == null || carrera.estadoActiva}">checked</c:if>>
                        <label class="form-check-label" for="estadoActiva">
                            Carrera Activa
                        </label>
                    </div>
                </div>
            </div>

            <div class="row mb-3">
                <!-- Campo: Descripción (Textarea) -->
                <div class="col-12">
                    <label for="descripcion" class="form-label">
                        <i class="bi bi-card-text"></i> Descripción General
                    </label>
                    <textarea class="form-control" 
                              id="descripcion" 
                              name="descripcion" 
                              rows="4" 
                              placeholder="Ingrese una descripción general de la carrera..."><c:out value="${carrera.descripcion}"/></textarea>
                </div>
            </div>

            <div class="d-flex justify-content-between mt-4">
                <a href="${pageContext.request.contextPath}/carreras?accion=listar" 
                   class="btn btn-secondary">
                    <i class="bi bi-arrow-left"></i> Cancelar
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-save"></i> ${esEdicion ? 'Actualizar' : 'Guardar'} Carrera
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    // Validación de al menos una modalidad seleccionada
    document.getElementById('formCarrera').addEventListener('submit', function(e) {
        const modalidades = document.querySelectorAll('input[name="modalidades"]:checked');
        if (modalidades.length === 0) {
            e.preventDefault();
            alert('Debe seleccionar al menos una modalidad.');
            return false;
        }
    });
</script>

