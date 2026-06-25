package vn.ewallet.gateway.filter;

import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    public static class Config {

    }
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            if (isPublicEndpoint(path)) {
                return chain.filter(exchange);
            }
            String authHeaders = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeaders == null || !authHeaders.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeaders.substring(7);
            if (!"dev-token".equals(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            var mutatedRequest = exchange.getRequest()
                    .mutate()
                    .header("X-User-Id", "dev-user-001")
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        });
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/api/v1/auth") || path.equals("/actuator/health");
    }

    public AuthenticationFilter() {
        super(Config.class);
    }
}
