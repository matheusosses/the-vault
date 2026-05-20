package br.com.matheusosses.the_vault.models.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserAuthoritiesTest {

    @Test
    void adminDeveTerRoleAdminERoleUser() {
        User admin = new User(1L, "admin", "senha", "ADMIN");

        assertThat(admin.getAuthorities())
            .extracting(a -> a.getAuthority())
            .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    void userDeveTerApenasRoleUser() {
        User user = new User(2L, "user", "senha", "USER");

        assertThat(user.getAuthorities())
            .extracting(a -> a.getAuthority())
            .containsExactly("ROLE_USER");
    }
}
