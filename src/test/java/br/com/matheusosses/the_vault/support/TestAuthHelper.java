package br.com.matheusosses.the_vault.support;

import br.com.matheusosses.the_vault.infra.security.TokenService;
import br.com.matheusosses.the_vault.models.user.User;
import br.com.matheusosses.the_vault.models.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestAuthHelper {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public TestAuthHelper(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public String createAdminToken() {
        return createToken("admin", "ADMIN");
    }

    private String createToken(String login, String role) {
        userRepository.deleteAll();
        User user = new User(null, login, passwordEncoder.encode("123456"), role);
        userRepository.save(user);
        return tokenService.getToken(user);
    }
}
