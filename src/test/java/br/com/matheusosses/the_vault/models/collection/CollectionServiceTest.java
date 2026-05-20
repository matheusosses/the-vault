package br.com.matheusosses.the_vault.models.collection;

import br.com.matheusosses.the_vault.models.collection.dto.CollectionDto;
import br.com.matheusosses.the_vault.models.collection.dto.NewCollectionDto;
import br.com.matheusosses.the_vault.models.game.Game;
import br.com.matheusosses.the_vault.models.game.GameCategory;
import br.com.matheusosses.the_vault.models.game.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollectionServiceTest {

    @Mock
    private CollectionRepository repository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private CollectionMapper mapper;

    @InjectMocks
    private CollectionService service;

    @Test
    void deveAdicionarJogoNaColecao() {
        Collection collection = new Collection(1L, "Favoritos", new HashSet<>(), Status.OWNED, null, null);
        Game game = new Game(10L, "Catan", "Desc", GameCategory.STRATEGY, 1995, null, null);
        CollectionDto dto = new CollectionDto(1L, "Favoritos", Status.OWNED, Set.of());

        when(repository.findById(1L)).thenReturn(Optional.of(collection));
        when(gameRepository.findById(10L)).thenReturn(Optional.of(game));
        when(mapper.toDto(collection)).thenReturn(dto);

        CollectionDto result = service.adicionarJogo(1L, 10L);

        assertThat(collection.getGames()).contains(game);
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void deveRemoverJogoDaColecao() {
        Game game = new Game(10L, "Catan", "Desc", GameCategory.STRATEGY, 1995, null, null);
        Set<Game> games = new HashSet<>();
        games.add(game);
        Collection collection = new Collection(1L, "Favoritos", games, Status.OWNED, null, null);
        CollectionDto dto = new CollectionDto(1L, "Favoritos", Status.OWNED, Set.of());

        when(repository.findById(1L)).thenReturn(Optional.of(collection));
        when(gameRepository.findById(10L)).thenReturn(Optional.of(game));
        when(mapper.toDto(collection)).thenReturn(dto);

        service.removerJogo(1L, 10L);

        assertThat(collection.getGames()).doesNotContain(game);
    }

    @Test
    void deveLancarExcecaoQuandoColecaoNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.adicionarJogo(99L, 10L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Coleção não encontrada");
    }

    @Test
    void deveLancarExcecaoQuandoJogoNaoExiste() {
        Collection collection = new Collection(1L, "Favoritos", new HashSet<>(), Status.OWNED, null, null);
        when(repository.findById(1L)).thenReturn(Optional.of(collection));
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.adicionarJogo(1L, 99L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Jogo não encontrado");
    }

    @Test
    void deveLancarExcecaoAoDetalharColecaoInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.detalhar(99L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Coleção não encontrada");
    }
}
