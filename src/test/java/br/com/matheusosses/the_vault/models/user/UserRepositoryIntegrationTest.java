package br.com.matheusosses.the_vault.models.user;

import br.com.matheusosses.the_vault.support.AbstractJpaIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryIntegrationTest extends AbstractJpaIntegrationTest {

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void deveBuscarUsuarioPorLogin() {
        repository.save(new User(null, "admin", "hash", "ADMIN"));

        UserDetails user = repository.findByLogin("admin");

        assertThat(user.getUsername()).isEqualTo("admin");
    }
}
