package br.com.matheusosses.the_vault.models.game;

import br.com.matheusosses.the_vault.support.AbstractJpaIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class GameRepositoryIntegrationTest extends AbstractJpaIntegrationTest {

    @Autowired
    private GameRepository repository;

    @Test
    void devePersistirJogo() {
        Game game = new Game(null, "Catan", "Desc", GameCategory.STRATEGY, 1995, null, null);

        Game saved = repository.save(game);

        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findById(saved.getId())).isPresent();
    }

    @Test
    void softDeleteDeveOcultarJogo() {
        Game game = repository.save(new Game(null, "Azul", "Desc", GameCategory.FAMILY, 2017, null, null));

        repository.delete(game);

        assertThat(repository.findById(game.getId())).isEmpty();
    }
}
