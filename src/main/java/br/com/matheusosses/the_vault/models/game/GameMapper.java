package br.com.matheusosses.the_vault.models.game;

import br.com.matheusosses.the_vault.models.game.dto.GameDto;
import br.com.matheusosses.the_vault.models.game.dto.NewGameDto;

import br.com.matheusosses.the_vault.models.game.dto.UpdateGameDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface GameMapper {

    Game toEntity(NewGameDto dto);

    GameDto toDto(Game game);

    void updateFromDto(UpdateGameDto dto, @MappingTarget Game game);
}
