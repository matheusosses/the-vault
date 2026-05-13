package br.com.matheusosses.the_vault.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CurrentYearOrPastValidator.class})
@Documented
public @interface CurrenYearOrPast {
    String message() default "O ano não pode ser maior que o atual";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
