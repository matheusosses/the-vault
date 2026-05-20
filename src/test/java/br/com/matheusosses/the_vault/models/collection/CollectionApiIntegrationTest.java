package br.com.matheusosses.the_vault.models.collection;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class CollectionApiIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestAuthHelper authHelper;

    @Autowired
    private GameRepository gameRepository;

    private String token;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
        token = authHelper.createAdminToken();
    }

    @Test
    void deveCriarColecaoEAdicionarRemoverJogo() throws Exception {
        Long gameId = gameRepository.save(new Game(null, "Catan", "Desc", GameCategory.STRATEGY, 1995, null, null)).getId();

        String location = mockMvc.perform(post("/collections")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Favoritos",
                      "status": "OWNED"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Favoritos"))
            .andReturn()
            .getResponse()
            .getHeader("Location");

        String collectionId = location.substring(location.lastIndexOf('/') + 1);

        mockMvc.perform(post("/collections/" + collectionId + "/games/" + gameId)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.games[0].title").value("Catan"));

        mockMvc.perform(delete("/collections/" + collectionId + "/games/" + gameId)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.games").isEmpty());
    }
}
