package br.com.matheusosses.the_vault.models.game.dto;

import br.com.matheusosses.the_vault.models.game.Game;
import br.com.matheusosses.the_vault.models.game.GameCategory;

public record GameDto(
    Long id,
    String title,
    String description,
    GameCategory category,
    Integer releaseYear) {

    public GameDto(Game game){
        this(game.getId(), game.getTitle(), game.getDescription(), game.getCategory(), game.getReleaseYear());
    }
}
