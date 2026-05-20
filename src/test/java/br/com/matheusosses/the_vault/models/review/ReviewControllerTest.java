package br.com.matheusosses.the_vault.models.review;

import br.com.matheusosses.the_vault.models.review.dto.NewReviewDto;
import br.com.matheusosses.the_vault.models.review.dto.ReviewDto;
import br.com.matheusosses.the_vault.models.review.dto.UpdateReviewDto;
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

import java.time.LocalDateTime;
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

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest extends WebMvcTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService service;

    @Test
    @WithMockUser
    void deveCriarReview() throws Exception {
        ReviewDto dto = new ReviewDto(1L, "Catan", 5, "Ótimo", LocalDateTime.now());
        when(service.criar(eq(1L), any(NewReviewDto.class))).thenReturn(dto);

        mockMvc.perform(post("/games/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "score": 5,
                      "comment": "Ótimo"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.score").value(5));
    }

    @Test
    @WithMockUser
    void deveListarReviews() throws Exception {
        ReviewDto dto = new ReviewDto(1L, "Catan", 5, "Ótimo", LocalDateTime.now());
        when(service.listar(eq(1L), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/games/1/reviews"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].score").value(5));
    }

    @Test
    @WithMockUser
    void deveDetalharReview() throws Exception {
        ReviewDto dto = new ReviewDto(1L, "Catan", 5, "Ótimo", LocalDateTime.now());
        when(service.detalhar(1L, 1L)).thenReturn(dto);

        mockMvc.perform(get("/games/1/reviews/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser
    void deveAtualizarReview() throws Exception {
        ReviewDto dto = new ReviewDto(1L, "Catan", 4, "Bom", LocalDateTime.now());
        when(service.atualizar(eq(1L), eq(1L), any(UpdateReviewDto.class))).thenReturn(dto);

        mockMvc.perform(put("/games/1/reviews/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "score": 4,
                      "comment": "Bom"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.score").value(4));
    }

    @Test
    @WithMockUser
    void deveExcluirReview() throws Exception {
        mockMvc.perform(delete("/games/1/reviews/1"))
            .andExpect(status().isNoContent());

        verify(service).excluir(1L, 1L);
    }

    @Test
    @WithMockUser
    void deveRetornar400ParaPayloadInvalido() throws Exception {
        mockMvc.perform(post("/games/1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
}
