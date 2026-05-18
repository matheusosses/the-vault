package br.com.matheusosses.the_vault.models.collection.dto;

import br.com.matheusosses.the_vault.models.collection.Status;
import br.com.matheusosses.the_vault.models.game.dto.GameDto;

import java.util.Set;

public record CollectionDto(
    Long id,
    String name,
    Status status,
    Set<GameDto> games
) {
}
