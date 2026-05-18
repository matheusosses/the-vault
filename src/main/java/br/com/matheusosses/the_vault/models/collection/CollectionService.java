package br.com.matheusosses.the_vault.models.collection;

import br.com.matheusosses.the_vault.models.collection.dto.CollectionDto;
import br.com.matheusosses.the_vault.models.collection.dto.NewCollectionDto;
import br.com.matheusosses.the_vault.models.game.Game;
import br.com.matheusosses.the_vault.models.game.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository repository;
    private final GameRepository gameRepository;
    private final CollectionMapper mapper;

    public CollectionDto criar(NewCollectionDto dto) {
        Collection collection = mapper.toEntity(dto);
        return mapper.toDto(repository.save(collection));
    }

    @Transactional
    public CollectionDto adicionarJogo(Long collectionId, Long gameId) {
        Collection collection = repository.findById(collectionId)
            .orElseThrow(() -> new EntityNotFoundException("Coleção não encontrada"));

        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));

        collection.getGames().add(game);

        return mapper.toDto(collection);
    }

    @Transactional
    public CollectionDto removerJogo(Long collectionId, Long gameId) {
        Collection collection = repository.findById(collectionId)
            .orElseThrow(() -> new EntityNotFoundException("Coleção não encontrada"));

        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));

        collection.getGames().remove(game);

        return mapper.toDto(collection);
    }
}
