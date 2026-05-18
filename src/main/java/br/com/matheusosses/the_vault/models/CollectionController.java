package br.com.matheusosses.the_vault.models;

import br.com.matheusosses.the_vault.models.collection.CollectionService;
import br.com.matheusosses.the_vault.models.collection.dto.CollectionDto;
import br.com.matheusosses.the_vault.models.collection.dto.NewCollectionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/collections")
public class CollectionController {

    private final CollectionService service;

    @PostMapping
    public ResponseEntity<CollectionDto> criar(@RequestBody @Valid NewCollectionDto dto,
                                               UriComponentsBuilder uriBuilder) {
        CollectionDto collection = service.criar(dto);
        URI uri = uriBuilder.path("/collections/{id}")
            .buildAndExpand(collection.id()).toUri();

        return ResponseEntity.created(uri).body(collection);
    }

    @GetMapping
    public ResponseEntity<Page<CollectionDto>> listar(
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pag) {
        Page<CollectionDto> page = service.listar(pag);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionDto> detalhar(@PathVariable Long id) {
        CollectionDto collection = service.detalhar(id);
        return ResponseEntity.ok(collection);
    }

    @PostMapping("/{collectionId}/games/{gameId}")
    public ResponseEntity<CollectionDto> adicionarJogo(@PathVariable Long collectionId,
                                                       @PathVariable Long gameId) {
        CollectionDto collection = service.adicionarJogo(collectionId, gameId);
        return ResponseEntity.ok(collection);
    }

    @DeleteMapping("/{collectionId}/games/{gameId}")
    public ResponseEntity<CollectionDto> removerJogo(@PathVariable Long collectionId,
                                                     @PathVariable Long gameId) {
        CollectionDto collection = service.removerJogo(collectionId, gameId);
        return ResponseEntity.ok(collection);
    }
}
