package vn.ewallet.auth.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import vn.ewallet.auth.dto.LoginRequest;
import vn.ewallet.auth.dto.RefreshTokenRequest;
import vn.ewallet.auth.dto.TokenResponse;

@Component
public class KeycloakAuthClient {
    private final WebClient webClient;

    @Value("${keycloak.realm}")
    private String realm;

    @Value(("${keycloak.client-id}"))
    private String clientId;

    public KeycloakAuthClient(@Value("${keycloak.base-url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public TokenResponse login(LoginRequest request) {
        var form = new LinkedMultiValueMap<String, String>();
        form.add("client-id", clientId);
        form.add("grant_type", "password");
        form.add("username", request.username());
        form.add("password", request.password());
        return webClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realm)
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    public TokenResponse refresh(RefreshTokenRequest request) {
        var form = new LinkedMultiValueMap<String, String>();
        form.add("client-id", clientId);
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", request.refreshToken());

        return webClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realm)
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }
}
