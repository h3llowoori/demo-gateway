package com.h3llowoori.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("book-api", r -> r
                        .order(1)
                        .path("/books/**")
                        .filters(f -> f.rewritePath("/books/(?<segment>.*)", "/v1/books/${segment}"))
                        .uri("lb://book-api"))
                .route("payment-api", r -> r
                        .order(1)
                        .path("/payments/**")
                        .filters(f -> f.rewritePath("/payments/(?<segment>.*)", "/v1/payments/${segment}"))
                        .uri("lb://payment-api"))
                .route("kakao-book-api", r -> r
                        .path("/kakao-books/**")
                        .filters(f -> f
                                // 1. 경로 재작성: /books/spring -> /v3/search/book?query=spring
                                .rewritePath("/books/(?<segment>.*)", "/v3/search/book?query=${segment}")
                                // 2. 카카오 인증 헤더 추가 (반드시 KakaoAK 뒤에 한 칸 띄우고 키 입력)
                                .addRequestHeader("Authorization", "KakaoAK YOUR_REST_API_KEY"))
                        .uri("https://dapi.kakao.com"))
                .build();
    }
}
