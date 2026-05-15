package br.com.matheusosses.the_vault.infra.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StandardError(
    LocalDateTime timestamp,
    Integer status,
    String error,
    String message,
    String path,
    List<ErrorResponseDto> validationErrors
) {}
