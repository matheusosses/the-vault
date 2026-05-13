package br.com.matheusosses.the_vault.models.review.dto;

import java.time.LocalDateTime;

public record ReviewDto(
    Long id,
    String gameTitle,
    Integer score,
    String comment,
    LocalDateTime createdAt) {
}
