package com.darwinruiz.miniecommerce.validators;

import com.darwinruiz.miniecommerce.services.ProjectService;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator("uniqueProjectNameValidator")
public class UniqueProjectNameValidator implements Validator<String> {

    @Override
    public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
        if (value == null || value.trim().isEmpty()) {
            return; // Dejamos que @NotNull o required maneje esto
        }

        // Obtener el ID del proyecto actual si está en edición
        Long currentId = (Long) component.getAttributes().get("currentProjectId");
        
        try {
            ProjectService projectService = CDI.current().select(ProjectService.class).get();
            projectService.validateUniqueName(value, currentId);
        } catch (IllegalArgumentException e) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Nombre duplicado", e.getMessage()));
        }
    }
}

