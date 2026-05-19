package br.com.matheusosses.the_vault.infra.security;

import br.com.matheusosses.the_vault.models.user.User;
import br.com.matheusosses.the_vault.models.user.dto.AuthDto;
import br.com.matheusosses.the_vault.models.user.dto.TokenJwtDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenJwtDto> login(@RequestBody @Valid AuthDto dto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJwt = tokenService.getToken((User) Objects.requireNonNull(authentication.getPrincipal()));

        return ResponseEntity.ok(new TokenJwtDto(tokenJwt));
    }
}
