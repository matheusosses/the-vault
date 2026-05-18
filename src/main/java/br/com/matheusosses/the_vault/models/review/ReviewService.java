package br.com.matheusosses.the_vault.models.review;

import br.com.matheusosses.the_vault.models.game.Game;
import br.com.matheusosses.the_vault.models.game.GameRepository;
import br.com.matheusosses.the_vault.models.review.dto.NewReviewDto;
import br.com.matheusosses.the_vault.models.review.dto.ReviewDto;
import br.com.matheusosses.the_vault.models.review.dto.UpdateReviewDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;
    private final GameRepository gameRepository;
    private final ReviewMapper mapper;

    @Transactional
    public ReviewDto criar(Long gameId, NewReviewDto dto) {
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));

        Review review = mapper.toEntity(dto);
        review.setGame(game);
        return mapper.toDto(repository.save(review));
    }

    @Transactional(readOnly = true)
    public Page<ReviewDto> listar(Long gameId, Pageable pag) {
        return repository.findAllByGameId(gameId, pag).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public ReviewDto detalhar(Long gameId, Long id) {
        Review review = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Review não encontrada"));

        validarVinculo(gameId, review);
        return mapper.toDto(review);
    }

    @Transactional
    public ReviewDto atualizar(Long gameId, Long id, UpdateReviewDto dto) {
        Review review = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Review não encontrada"));

        validarVinculo(gameId, review);
        mapper.updateFromDto(dto, review);
        return mapper.toDto(review);
    }

    @Transactional
    public void excluir(Long gameId, Long id) {
        Review review = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Review não encontrada"));

        validarVinculo(gameId, review);
        repository.delete(review);
    }

    private void validarVinculo(Long gameId, Review review) {
        if (!review.getGame().getId().equals(gameId)) {
            throw new IllegalArgumentException("Esta review não pertence ao jogo informado.");
        }
    }
}
