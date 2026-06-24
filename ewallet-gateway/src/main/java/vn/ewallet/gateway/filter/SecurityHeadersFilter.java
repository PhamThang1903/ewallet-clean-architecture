package vn.ewallet.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SecurityHeadersFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    var headers = exchange.getResponse().getHeaders();
                    headers.add("X-Content-Type-Options", "nosniff");
                    headers.add("X-Frame-Options", "DENY");
                    headers.add("Referrer-Policy", "strict-origin-when-cross-origin");
                    headers.add("Permissions-Policy", "geolocation=(), microphone=()");
                }));
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
