package br.com.matheusosses.the_vault.models.collection.dto;

import br.com.matheusosses.the_vault.models.collection.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewCollectionDto(
    @NotBlank(message = "O nome da coleção é obrigatório")
    String name,

    @NotNull(message = "O status é obrigatório")
    Status status
) {
}
