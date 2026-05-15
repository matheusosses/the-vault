package br.com.matheusosses.the_vault.infra.dto;

import org.springframework.validation.FieldError;

public record ErrorResponseDto(String field, String message) {
    public ErrorResponseDto(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}
