package br.com.matheusosses.the_vault.models.collection;

import br.com.matheusosses.the_vault.models.collection.dto.CollectionDto;
import br.com.matheusosses.the_vault.models.collection.dto.NewCollectionDto;
import br.com.matheusosses.the_vault.models.game.GameMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {GameMapper.class}
)
public interface CollectionMapper {

    CollectionDto toDto(Collection collection);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "games", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Collection toEntity(NewCollectionDto dto);
}
