package br.com.matheusosses.the_vault.models.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateReviewDto(
    @Min(1)
    @Max(5)
    Integer score,

    @Size(max = 1000)
    String comment
) {
}
