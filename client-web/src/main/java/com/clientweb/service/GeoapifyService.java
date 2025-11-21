package com.clientweb.service;

import com.clientweb.dto.GeoapifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class GeoapifyService {
    private final WebClient.Builder webClientBuilder;

    @Value("${geoapify.api.key}")
    private String apiKey;

    @Value("${geoapify.api.url}")
    private String apiUrl;

    public GeoapifyResponse searchRestaurants(double lat, double lon, String cuisine, int radius) {
        WebClient webClient = webClientBuilder.baseUrl(apiUrl).build();

        String categories = "catering.restaurant";
        if (cuisine != null && !cuisine.isEmpty()) {
            categories += "." + cuisine.toLowerCase();
        }

        String filter = String.format("circle:%.6f,%.6f,%d", lon, lat, radius);

        String finalCategories = categories;
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("categories", finalCategories)
                        .queryParam("filter", filter)
                        .queryParam("limit", 20)
                        .queryParam("apiKey", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(GeoapifyResponse.class)
                .block();
    }
}