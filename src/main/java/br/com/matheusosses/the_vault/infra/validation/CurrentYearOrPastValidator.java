package br.com.matheusosses.the_vault.infra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class CurrentYearOrPastValidator implements ConstraintValidator<CurrentYearOrPast, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return value <= LocalDate.now().getYear(); // Pega o ano atual dinamicamente
    }
}
