package br.com.matheusosses.the_vault.models.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewReviewDto(
@NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "A nota mínima é 1")
    @Max(value = 5, message = "A nota máxima é 5")
    Integer score,

    @Size(max = 1000, message = "O comentário não pode exceder 1000 caracteres")
    String comment) {
}
