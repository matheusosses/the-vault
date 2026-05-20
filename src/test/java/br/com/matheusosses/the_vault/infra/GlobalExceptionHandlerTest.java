package br.com.matheusosses.the_vault.infra;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ExceptionStubController())
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Test
    void deveRetornar400ParaValidacao() throws Exception {
        mockMvc.perform(post("/test/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Erro de Validação"))
            .andExpect(jsonPath("$.validationErrors").isArray());
    }

    @Test
    void deveRetornar404ParaEntityNotFound() throws Exception {
        mockMvc.perform(get("/test/not-found"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Recurso ausente"));
    }

    @Test
    void deveRetornar400ParaIllegalArgument() throws Exception {
        mockMvc.perform(get("/test/illegal-argument"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Argumento inválido"));
    }

    @Test
    void deveRetornar409ParaDataIntegrityViolation() throws Exception {
        mockMvc.perform(get("/test/conflict"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.error").value("Conflito de Integridade"));
    }

    @Test
    void deveRetornar500ParaExcecaoGenerica() throws Exception {
        mockMvc.perform(get("/test/error"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.error").value("Erro Interno"));
    }

    @RestController
    @RequestMapping("/test")
    static class ExceptionStubController {

        record StubDto(@NotBlank String name) {}

        @PostMapping("/validation")
        void validation(@RequestBody @Valid StubDto dto) {}

        @GetMapping("/not-found")
        void notFound() {
            throw new EntityNotFoundException("Recurso ausente");
        }

        @GetMapping("/illegal-argument")
        void illegalArgument() {
            throw new IllegalArgumentException("Argumento inválido");
        }

        @GetMapping("/conflict")
        void conflict() {
            throw new DataIntegrityViolationException("conflito");
        }

        @GetMapping("/error")
        void error() {
            throw new RuntimeException("erro");
        }
    }
}
