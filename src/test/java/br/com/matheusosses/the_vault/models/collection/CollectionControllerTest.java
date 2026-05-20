package br.com.matheusosses.the_vault.models.collection;

import br.com.matheusosses.the_vault.models.collection.dto.CollectionDto;
import br.com.matheusosses.the_vault.models.collection.dto.NewCollectionDto;
import br.com.matheusosses.the_vault.support.WebMvcTestSupport;
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
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CollectionController.class)
@AutoConfigureMockMvc(addFilters = false)
class CollectionControllerTest extends WebMvcTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CollectionService service;

    @Test
    @WithMockUser
    void deveCriarColecao() throws Exception {
        CollectionDto dto = new CollectionDto(1L, "Favoritos", Status.OWNED, Set.of());
        when(service.criar(any(NewCollectionDto.class))).thenReturn(dto);

        mockMvc.perform(post("/collections")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Favoritos",
                      "status": "OWNED"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.name").value("Favoritos"));
    }

    @Test
    @WithMockUser
    void deveListarColecoes() throws Exception {
        CollectionDto dto = new CollectionDto(1L, "Favoritos", Status.OWNED, Set.of());
        when(service.listar(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/collections"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("Favoritos"));
    }

    @Test
    @WithMockUser
    void deveDetalharColecao() throws Exception {
        CollectionDto dto = new CollectionDto(1L, "Favoritos", Status.OWNED, Set.of());
        when(service.detalhar(1L)).thenReturn(dto);

        mockMvc.perform(get("/collections/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void deveAdicionarJogoNaColecao() throws Exception {
        CollectionDto dto = new CollectionDto(1L, "Favoritos", Status.OWNED, Set.of());
        when(service.adicionarJogo(1L, 10L)).thenReturn(dto);

        mockMvc.perform(post("/collections/1/games/10"))
            .andExpect(status().isOk());

        verify(service).adicionarJogo(1L, 10L);
    }

    @Test
    @WithMockUser
    void deveRemoverJogoDaColecao() throws Exception {
        CollectionDto dto = new CollectionDto(1L, "Favoritos", Status.OWNED, Set.of());
        when(service.removerJogo(1L, 10L)).thenReturn(dto);

        mockMvc.perform(delete("/collections/1/games/10"))
            .andExpect(status().isOk());

        verify(service).removerJogo(1L, 10L);
    }

    @Test
    @WithMockUser
    void deveRetornar400ParaPayloadInvalido() throws Exception {
        mockMvc.perform(post("/collections")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
}
