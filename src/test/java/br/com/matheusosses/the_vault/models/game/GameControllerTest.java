package br.com.matheusosses.the_vault.models.game;

import br.com.matheusosses.the_vault.models.game.dto.GameDto;
import br.com.matheusosses.the_vault.models.game.dto.NewGameDto;
import br.com.matheusosses.the_vault.models.game.dto.UpdateGameDto;
import br.com.matheusosses.the_vault.support.WebMvcTestSupport;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
@AutoConfigureMockMvc(addFilters = false)
class GameControllerTest extends WebMvcTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService service;

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveCadastrarJogoComStatus201() throws Exception {
        GameDto dto = new GameDto(1L, "Catan", "Desc", GameCategory.STRATEGY, 1995);
        when(service.criar(any(NewGameDto.class))).thenReturn(dto);

        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "title": "Catan",
                      "description": "Desc",
                      "category": "STRATEGY",
                      "releaseYear": 1995
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "http://localhost/games/1"))
            .andExpect(jsonPath("$.title").value("Catan"));
    }

    @Test
    @WithMockUser
    void deveListarJogos() throws Exception {
        GameDto dto = new GameDto(1L, "Catan", "Desc", GameCategory.STRATEGY, 1995);
        when(service.listar(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/games"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].title").value("Catan"));
    }

    @Test
    @WithMockUser
    void deveDetalharJogo() throws Exception {
        GameDto dto = new GameDto(1L, "Catan", "Desc", GameCategory.STRATEGY, 1995);
        when(service.detalhar(1L)).thenReturn(dto);

        mockMvc.perform(get("/games/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void deveAtualizarJogo() throws Exception {
        GameDto dto = new GameDto(1L, "Novo", "Desc", GameCategory.FAMILY, 2000);
        when(service.atualizar(eq(1L), any(UpdateGameDto.class))).thenReturn(dto);

        mockMvc.perform(put("/games/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "title": "Novo",
                      "description": "Desc",
                      "category": "FAMILY",
                      "releaseYear": 2000
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Novo"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveExcluirJogo() throws Exception {
        mockMvc.perform(delete("/games/1"))
            .andExpect(status().isNoContent());

        verify(service).excluir(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deveRetornar400ParaPayloadInvalido() throws Exception {
        mockMvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void deveRetornar404QuandoJogoNaoExiste() throws Exception {
        when(service.detalhar(99L)).thenThrow(new EntityNotFoundException("Jogo não encontrado"));

        mockMvc.perform(get("/games/99"))
            .andExpect(status().isNotFound());
    }
}
