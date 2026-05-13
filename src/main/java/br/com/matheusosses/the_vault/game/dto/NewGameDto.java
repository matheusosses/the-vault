package br.com.matheusosses.the_vault.game.dto;

import br.com.matheusosses.the_vault.game.GameCategory;
import br.com.matheusosses.the_vault.infra.validation.CurrenYearOrPast;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewGameDto(
    @NotBlank(message = "O título é obrigatório")
    @Size(min = 2, max = 150, message = "O título deve ter entre 2 e 150 caracteres")
    String title,

    @NotBlank(message = "A descrição não pode estar em branco")
    String description,

    @NotNull(message = "A categoria é obrigatória")
    GameCategory category,

    @NotNull(message = "O ano de lançamento é obrigatório")
    @CurrenYearOrPast
    Integer releaseYear) {
}
