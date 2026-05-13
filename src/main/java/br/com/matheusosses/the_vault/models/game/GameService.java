package br.com.matheusosses.the_vault.models.game;

import br.com.matheusosses.the_vault.models.game.dto.GameDto;
import br.com.matheusosses.the_vault.models.game.dto.NewGameDto;
import br.com.matheusosses.the_vault.models.game.dto.UpdateGameDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository repository;
    private final GameMapper mapper;

    @Transactional
    public GameDto criar(NewGameDto dto){
        Game game = mapper.toEntity(dto);
        return mapper.toDto(repository.save(game));
    }

    @Transactional(readOnly = true)
    public Page<GameDto> listar(Pageable pag){
        return repository.findAll(pag)
            .map(mapper::toDto);

    }

    @Transactional(readOnly = true)
    public GameDto detalhar(Long id){
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));
    }

    @Transactional
    public void excluir(Long id){
        if(!repository.existsById(id)){
            throw new EntityNotFoundException("Jogo não encontrado");
        }
        repository.deleteById(id);
    }

    @Transactional
    public GameDto atualizar(Long id, UpdateGameDto dto) {
        Game game = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));

        mapper.updateFromDto(dto, game);
        return mapper.toDto(repository.save(game));
    }
}
