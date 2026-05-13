package br.com.matheusosses.the_vault.game.dto;

import br.com.matheusosses.the_vault.game.GameCategory;

public record GameDto(
    Long id,
    String title,
    String description,
    GameCategory category,
    Integer releaseYear) {
}
