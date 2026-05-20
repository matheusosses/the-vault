package br.com.matheusosses.the_vault.models.review;

import br.com.matheusosses.the_vault.models.game.Game;
import br.com.matheusosses.the_vault.models.game.GameCategory;
import br.com.matheusosses.the_vault.models.game.GameRepository;
import br.com.matheusosses.the_vault.support.AbstractIntegrationTest;
import br.com.matheusosses.the_vault.support.TestAuthHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class ReviewApiIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestAuthHelper authHelper;

    @Autowired
    private GameRepository gameRepository;

    private String token;
    private Long gameId;
    private Long otherGameId;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
        token = authHelper.createAdminToken();
        gameId = gameRepository.save(new Game(null, "Catan", "Desc", GameCategory.STRATEGY, 1995, null, null)).getId();
        otherGameId = gameRepository.save(new Game(null, "Azul", "Desc", GameCategory.FAMILY, 2017, null, null)).getId();
    }

    @Test
    void deveCriarEListarReviews() throws Exception {
        mockMvc.perform(post("/games/" + gameId + "/reviews")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "score": 5,
                      "comment": "Ótimo"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.score").value(5));

        mockMvc.perform(get("/games/" + gameId + "/reviews")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].score").value(5));
    }

    @Test
    void deveRetornar400QuandoReviewNaoPertenceAoJogo() throws Exception {
        String location = mockMvc.perform(post("/games/" + otherGameId + "/reviews")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "score": 4,
                      "comment": "Bom"
                    }
                    """))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");

        String reviewId = location.substring(location.lastIndexOf('/') + 1);

        mockMvc.perform(get("/games/" + gameId + "/reviews/" + reviewId)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Esta review não pertence ao jogo informado."));
    }
}
