package br.com.matheusosses.the_vault.infra.security;

import br.com.matheusosses.the_vault.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "test-secret-min-32-chars-for-hmac256");
    }

    @Test
    void deveGerarTokenEExtrairSubject() {
        User user = new User(1L, "admin", "senha", "ADMIN");

        String token = tokenService.getToken(user);
        String subject = tokenService.getSubject(token);

        assertThat(token).isNotBlank();
        assertThat(subject).isEqualTo("admin");
    }

    @Test
    void deveLancarExcecaoParaTokenInvalido() {
        assertThatThrownBy(() -> tokenService.getSubject("token-invalido"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Token JWT inválido ou expirado");
    }
}
