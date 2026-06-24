package vn.ewallet.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final String CORRELATION_ID = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId = exchange.getRequest().getHeaders().getFirst(CORRELATION_ID);
        if (correlationId == null || correlationId.isBlank())
            correlationId = UUID.randomUUID().toString();
        String finalCorrelationId = correlationId;
        var mutatedRequest = exchange.getRequest().mutate().header(CORRELATION_ID, finalCorrelationId).build();
        log.info("Incoming request method={} path={} correlationId={}", mutatedRequest.getMethod(),
                mutatedRequest.getURI().getPath(),
                finalCorrelationId
        );
        return chain.filter(exchange.mutate().request(mutatedRequest).build())
                .then(Mono.fromRunnable(() -> log.info("Completed request path={} status={} correlationId={}",
                        mutatedRequest.getURI().getPath(),
                        exchange.getResponse().getStatusCode(),
                        finalCorrelationId)));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
