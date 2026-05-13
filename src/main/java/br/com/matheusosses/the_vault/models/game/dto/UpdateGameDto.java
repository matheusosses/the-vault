package br.com.matheusosses.the_vault.models.game.dto;

import br.com.matheusosses.the_vault.infra.validation.CurrentYearOrPast;
import br.com.matheusosses.the_vault.models.game.GameCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateGameDto(
    @NotBlank(message = "O título não pode ser vazio")
    String title,

    @NotBlank(message = "A descrição é obrigatória")
    String description,

    @NotNull(message = "A categoria é obrigatória")
    GameCategory category,

    @NotNull(message = "O ano é obrigatório")
    @CurrentYearOrPast
    Integer releaseYear
) {

}
