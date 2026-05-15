package br.com.matheusosses.the_vault.models.game;

import br.com.matheusosses.the_vault.models.game.dto.GameDto;
import br.com.matheusosses.the_vault.models.game.dto.NewGameDto;
import br.com.matheusosses.the_vault.models.game.dto.UpdateGameDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService service;

    @PostMapping
    public ResponseEntity<GameDto> cadastrar(@RequestBody @Valid NewGameDto dto,
                                             UriComponentsBuilder uriBuilder){
        GameDto gameDto = service.criar(dto);
        var uri = uriBuilder.path("/games/{id}").buildAndExpand(gameDto.id()).toUri();
        return ResponseEntity.created(uri).body(gameDto);
    }

    @GetMapping
    public ResponseEntity<Page<GameDto>> listar(
        @PageableDefault(size = 10, sort = {"title"}) Pageable paginacao) {
        return ResponseEntity.ok(service.listar(paginacao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDto> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalhar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameDto> atualizar(@PathVariable Long id, @RequestBody @Valid UpdateGameDto dados) {
        return ResponseEntity.ok(service.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
