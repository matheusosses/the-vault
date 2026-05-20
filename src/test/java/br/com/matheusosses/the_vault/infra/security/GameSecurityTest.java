package br.com.matheusosses.the_vault.infra.security;

import br.com.matheusosses.the_vault.models.game.GameCategory;
import br.com.matheusosses.the_vault.models.game.GameController;
import br.com.matheusosses.the_vault.models.game.GameService;
import br.com.matheusosses.the_vault.models.game.dto.GameDto;
import br.com.matheusosses.the_vault.support.WebMvcTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
@Import({SecurityConfigurations.class, SecurityFilter.class})
class GameSecurityTest extends WebMvcTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService service;

    @Test
    @WithAnonymousUser
    void deveRetornar403SemAutenticacao() throws Exception {
        mockMvc.perform(get("/games"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userNaoDeveCadastrarJogo() throws Exception {
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
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveCadastrarJogo() throws Exception {
        GameDto dto = new GameDto(1L, "Catan", "Desc", GameCategory.STRATEGY, 1995);
        when(service.criar(any())).thenReturn(dto);

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
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userNaoDeveExcluirJogo() throws Exception {
        mockMvc.perform(delete("/games/1"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDeveExcluirJogo() throws Exception {
        mockMvc.perform(delete("/games/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userDeveListarJogos() throws Exception {
        mockMvc.perform(get("/games"))
            .andExpect(status().isOk());
    }
}
