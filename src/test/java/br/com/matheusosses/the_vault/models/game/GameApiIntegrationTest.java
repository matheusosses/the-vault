package br.com.matheusosses.the_vault.models.game;

import br.com.matheusosses.the_vault.support.AbstractIntegrationTest;
import br.com.matheusosses.the_vault.support.TestAuthHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class GameApiIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestAuthHelper authHelper;

    @Autowired
    private GameRepository gameRepository;

    private String adminToken;

    @BeforeEach
    void setUp() {
        gameRepository.deleteAll();
        adminToken = authHelper.createAdminToken();
    }

    @Test
    void fluxoCompletoDeJogo() throws Exception {
        String location = mockMvc.perform(post("/games")
                .header("Authorization", "Bearer " + adminToken)
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
            .andExpect(jsonPath("$.title").value("Catan"))
            .andReturn()
            .getResponse()
            .getHeader("Location");

        mockMvc.perform(get("/games")
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].title").value("Catan"));

        String id = location.substring(location.lastIndexOf('/') + 1);

        mockMvc.perform(put("/games/" + id)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "title": "Catan Revised",
                      "description": "Nova desc",
                      "category": "STRATEGY",
                      "releaseYear": 1996
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Catan Revised"));

        mockMvc.perform(delete("/games/" + id)
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/games/" + id)
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isNotFound());
    }
}
