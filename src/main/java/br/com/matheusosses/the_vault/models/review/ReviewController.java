package br.com.matheusosses.the_vault.models.review;

import br.com.matheusosses.the_vault.models.review.dto.NewReviewDto;
import br.com.matheusosses.the_vault.models.review.dto.ReviewDto;
import br.com.matheusosses.the_vault.models.review.dto.UpdateReviewDto;
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
@RequestMapping("/games/{gameId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @PostMapping
    public ResponseEntity<ReviewDto> criar(@PathVariable Long gameId,
                                           @RequestBody @Valid NewReviewDto dto,
                                           UriComponentsBuilder uriBuilder) {
        ReviewDto review = service.criar(gameId, dto);
        URI uri = uriBuilder.path("/games/{gameId}/reviews/{id}")
            .buildAndExpand(gameId, review.id()).toUri();

        return ResponseEntity.created(uri).body(review);
    }

    @GetMapping
    public ResponseEntity<Page<ReviewDto>> listar(@PathVariable Long gameId,
                                                  @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pag) {
        var page = service.listar(gameId, pag);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> detalhar(@PathVariable Long gameId, @PathVariable Long id) {
        var review = service.detalhar(gameId, id);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> atualizar(@PathVariable Long gameId,
                                               @PathVariable Long id,
                                               @RequestBody @Valid UpdateReviewDto dto) {
        var review = service.atualizar(gameId, id, dto);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long gameId, @PathVariable Long id) {
        service.excluir(gameId, id);
        return ResponseEntity.noContent().build();
    }
}
