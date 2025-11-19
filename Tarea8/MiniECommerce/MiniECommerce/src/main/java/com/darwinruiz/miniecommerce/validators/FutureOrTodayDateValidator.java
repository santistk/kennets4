package com.darwinruiz.miniecommerce.validators;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

import java.time.LocalDate;

@FacesValidator("futureOrTodayDateValidator")
public class FutureOrTodayDateValidator implements Validator<LocalDate> {

    @Override
    public void validate(FacesContext context, UIComponent component, LocalDate value) throws ValidatorException {
        if (value == null) {
            return; // Dejamos que required maneje esto
        }

        if (value.isBefore(LocalDate.now())) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Fecha inválida", "La fecha de vencimiento no puede ser anterior a hoy"));
        }
    }
}

