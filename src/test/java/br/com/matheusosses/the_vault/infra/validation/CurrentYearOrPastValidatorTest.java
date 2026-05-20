package br.com.matheusosses.the_vault.infra.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CurrentYearOrPastValidatorTest {

    private CurrentYearOrPastValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CurrentYearOrPastValidator();
    }

    @Test
    void deveAceitarNull() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    void deveAceitarAnoAtual() {
        assertThat(validator.isValid(LocalDate.now().getYear(), null)).isTrue();
    }

    @Test
    void deveAceitarAnoPassado() {
        assertThat(validator.isValid(2000, null)).isTrue();
    }

    @Test
    void deveRejeitarAnoFuturo() {
        assertThat(validator.isValid(LocalDate.now().getYear() + 1, null)).isFalse();
    }
}
