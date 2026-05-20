package br.com.matheusosses.the_vault.models.review;

import br.com.matheusosses.the_vault.models.game.Game;
import br.com.matheusosses.the_vault.models.game.GameCategory;
import br.com.matheusosses.the_vault.models.game.GameRepository;
import br.com.matheusosses.the_vault.support.AbstractJpaIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewRepositoryIntegrationTest extends AbstractJpaIntegrationTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private GameRepository gameRepository;

    private Game game;

    @BeforeEach
    void setUp() {
        game = gameRepository.save(new Game(null, "Catan", "Desc", GameCategory.STRATEGY, 1995, null, null));
    }

    @Test
    void deveBuscarReviewsPorGameId() {
        reviewRepository.save(new Review(null, game, 5, "Ótimo", null, null));
        reviewRepository.save(new Review(null, game, 4, "Bom", null, null));

        Page<Review> page = reviewRepository.findAllByGameId(game.getId(), PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isEqualTo(2);
    }
}
