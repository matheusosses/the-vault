package br.com.matheusosses.the_vault.models.review;

import br.com.matheusosses.the_vault.models.review.dto.NewReviewDto;
import br.com.matheusosses.the_vault.models.review.dto.ReviewDto;
import br.com.matheusosses.the_vault.models.review.dto.UpdateReviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDto toDto(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "game", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Review toEntity(NewReviewDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "game", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateFromDto(UpdateReviewDto dto, @MappingTarget Review review);
}
