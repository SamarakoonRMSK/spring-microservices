package com.order.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
//    @Bean
//    public WebClient webClient(){
//        return WebClient.builder()
//                .baseUrl("http://localhost:8080")
//                .build();
//    }
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }


    @Bean
    public WebClient inventoryWebClient(){
        return webClientBuilder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    @Bean
    public WebClient productWebClient(){
        return webClientBuilder()
                .baseUrl("http://localhost:8082")
                .build();
    }
}
