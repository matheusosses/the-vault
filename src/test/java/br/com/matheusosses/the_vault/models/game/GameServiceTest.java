package br.com.matheusosses.the_vault.models.game;

import br.com.matheusosses.the_vault.models.game.dto.GameDto;
import br.com.matheusosses.the_vault.models.game.dto.NewGameDto;
import br.com.matheusosses.the_vault.models.game.dto.UpdateGameDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository repository;

    @Mock
    private GameMapper mapper;

    @InjectMocks
    private GameService service;

    @Test
    void deveCriarJogo() {
        NewGameDto dto = new NewGameDto("Catan", "Desc", GameCategory.STRATEGY, 1995);
        Game entity = new Game(null, "Catan", "Desc", GameCategory.STRATEGY, 1995, null, null);
        Game saved = new Game(1L, "Catan", "Desc", GameCategory.STRATEGY, 1995, null, null);
        GameDto gameDto = new GameDto(1L, "Catan", "Desc", GameCategory.STRATEGY, 1995);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(gameDto);

        GameDto result = service.criar(dto);

        assertThat(result.id()).isEqualTo(1L);
        verify(repository).save(entity);
    }

    @Test
    void deveLancarExcecaoAoDetalharJogoInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.detalhar(99L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Jogo não encontrado");
    }

    @Test
    void deveAtualizarJogoExistente() {
        UpdateGameDto dto = new UpdateGameDto("Novo", "Desc nova", GameCategory.FAMILY, 2000);
        Game game = new Game(1L, "Antigo", "Desc", GameCategory.STRATEGY, 1995, null, null);
        GameDto gameDto = new GameDto(1L, "Novo", "Desc nova", GameCategory.FAMILY, 2000);

        when(repository.findById(1L)).thenReturn(Optional.of(game));
        when(repository.save(game)).thenReturn(game);
        when(mapper.toDto(game)).thenReturn(gameDto);

        GameDto result = service.atualizar(1L, dto);

        assertThat(result.title()).isEqualTo("Novo");
        verify(mapper).updateFromDto(dto, game);
    }

    @Test
    void deveExcluirJogoExistente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.excluir(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoExcluirJogoInexistente() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.excluir(99L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Jogo não encontrado");
    }

    @Test
    void deveListarJogos() {
        Game game = new Game(1L, "Catan", "Desc", GameCategory.STRATEGY, 1995, null, null);
        GameDto gameDto = new GameDto(1L, "Catan", "Desc", GameCategory.STRATEGY, 1995);
        Page<Game> page = new PageImpl<>(List.of(game));

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.toDto(game)).thenReturn(gameDto);

        Page<GameDto> result = service.listar(Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
    }
}
