package br.com.matheusosses.the_vault.models.review;

import br.com.matheusosses.the_vault.models.game.Game;
import br.com.matheusosses.the_vault.models.game.GameCategory;
import br.com.matheusosses.the_vault.models.game.GameRepository;
import br.com.matheusosses.the_vault.models.review.dto.NewReviewDto;
import br.com.matheusosses.the_vault.models.review.dto.ReviewDto;
import br.com.matheusosses.the_vault.models.review.dto.UpdateReviewDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository repository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private ReviewMapper mapper;

    @InjectMocks
    private ReviewService service;

    @Test
    void deveCriarReviewParaJogoExistente() {
        Game game = new Game(1L, "Catan", "Desc", GameCategory.STRATEGY, 1995, null, null);
        NewReviewDto dto = new NewReviewDto(5, "Ótimo");
        Review review = new Review(null, game, 5, "Ótimo", null, null);
        Review saved = new Review(10L, game, 5, "Ótimo", LocalDateTime.now(), null);
        ReviewDto reviewDto = new ReviewDto(10L, "Catan", 5, "Ótimo", saved.getCreatedAt());

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(mapper.toEntity(dto)).thenReturn(review);
        when(repository.save(review)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(reviewDto);

        ReviewDto result = service.criar(1L, dto);

        assertThat(result.id()).isEqualTo(10L);
        verify(repository).save(review);
    }

    @Test
    void deveLancarExcecaoAoCriarReviewParaJogoInexistente() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criar(99L, new NewReviewDto(5, "Ótimo")))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Jogo não encontrado");
    }

    @Test
    void deveLancarExcecaoQuandoReviewNaoPertenceAoJogo() {
        Game game2 = new Game(2L, "Azul", "Desc", GameCategory.FAMILY, 2017, null, null);
        Review review = new Review(10L, game2, 4, "Bom", LocalDateTime.now(), null);

        when(repository.findById(10L)).thenReturn(Optional.of(review));

        assertThatThrownBy(() -> service.detalhar(1L, 10L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Esta review não pertence ao jogo informado.");
    }

    @Test
    void deveAtualizarReviewDoJogoCorreto() {
        Game game = new Game(1L, "Catan", "Desc", GameCategory.STRATEGY, 1995, null, null);
        Review review = new Review(10L, game, 4, "Bom", LocalDateTime.now(), null);
        UpdateReviewDto dto = new UpdateReviewDto(5, "Excelente");
        ReviewDto reviewDto = new ReviewDto(10L, "Catan", 5, "Excelente", review.getCreatedAt());

        when(repository.findById(10L)).thenReturn(Optional.of(review));
        when(mapper.toDto(review)).thenReturn(reviewDto);

        ReviewDto result = service.atualizar(1L, 10L, dto);

        assertThat(result.score()).isEqualTo(5);
        verify(mapper).updateFromDto(dto, review);
    }

    @Test
    void deveExcluirReviewDoJogoCorreto() {
        Game game = new Game(1L, "Catan", "Desc", GameCategory.STRATEGY, 1995, null, null);
        Review review = new Review(10L, game, 4, "Bom", LocalDateTime.now(), null);

        when(repository.findById(10L)).thenReturn(Optional.of(review));

        service.excluir(1L, 10L);

        verify(repository).delete(review);
    }

    @Test
    void deveLancarExcecaoAoAtualizarReviewDeOutroJogo() {
        Game game2 = new Game(2L, "Azul", "Desc", GameCategory.FAMILY, 2017, null, null);
        Review review = new Review(10L, game2, 4, "Bom", LocalDateTime.now(), null);

        when(repository.findById(10L)).thenReturn(Optional.of(review));

        assertThatThrownBy(() -> service.atualizar(1L, 10L, new UpdateReviewDto(5, "Excelente")))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
