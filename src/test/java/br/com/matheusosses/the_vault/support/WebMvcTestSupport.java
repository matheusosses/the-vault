package br.com.matheusosses.the_vault.support;

import br.com.matheusosses.the_vault.infra.security.TokenService;
import br.com.matheusosses.the_vault.models.user.UserRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public abstract class WebMvcTestSupport {

    @MockitoBean
    protected TokenService tokenService;

    @MockitoBean
    protected UserRepository userRepository;
}
