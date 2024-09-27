package com.bancolombia.tallerFinal.config;

import com.bancolombia.tallerFinal.exceptions.Error400Exception;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient createWebClient(WebClient.Builder builder){
        return builder.baseUrl("http://localhost:8091/transactions")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(error -> Mono.error(new Error400Exception(error)));
                })
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                            .flatMap(error -> Mono.error(new RuntimeException(error)));
                })
                .build();
    }

}
