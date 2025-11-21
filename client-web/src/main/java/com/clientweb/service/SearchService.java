package com.clientweb.service;

import com.clientweb.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final GeoapifyService geoapifyService;
    private final WebClient.Builder webClientBuilder;

    @Value("${restaurant.service.url}")
    private String restaurantServiceUrl;

    public List<RestaurantSearchDTO> searchRestaurants(
            double lat, double lon, String cuisine, int radius) {

        // Get restaurants from Geoapify API
        GeoapifyResponse apiResponse = geoapifyService.searchRestaurants(lat, lon, cuisine, radius);
        List<RestaurantSearchDTO> apiRestaurants = convertGeoapifyToDTO(apiResponse);

        // Get restaurants from database
        WebClient webClient = webClientBuilder.baseUrl(restaurantServiceUrl).build();
        List<RestaurantSearchDTO> dbRestaurants = webClient.get()
                .uri("/api/restaurants")
                .retrieve()
                .bodyToFlux(RestaurantSearchDTO.class)
                .collectList()
                .block();

        // Create a set of placeIds from API results to avoid duplicates
        Set<String> apiPlaceIds = apiRestaurants.stream()
                .map(RestaurantSearchDTO::getPlaceId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Filter database restaurants to exclude those already in API results
        List<RestaurantSearchDTO> filteredDbRestaurants = dbRestaurants != null ?
                dbRestaurants.stream()
                        .filter(r -> r.getPlaceId() == null || !apiPlaceIds.contains(r.getPlaceId()))
                        .collect(Collectors.toList()) :
                new ArrayList<>();

        // Combine results: API restaurants first, then database restaurants
        List<RestaurantSearchDTO> combined = new ArrayList<>(apiRestaurants);
        combined.addAll(filteredDbRestaurants);

        // Sort by rating (descending)
        combined.sort((r1, r2) -> {
            Double rating1 = r1.getRating() != null ? r1.getRating() : 0.0;
            Double rating2 = r2.getRating() != null ? r2.getRating() : 0.0;
            return rating2.compareTo(rating1);
        });

        return combined;
    }

    private List<RestaurantSearchDTO> convertGeoapifyToDTO(GeoapifyResponse response) {
        if (response == null || response.getFeatures() == null) {
            return new ArrayList<>();
        }

        return response.getFeatures().stream()
                .map(feature -> {
                    RestaurantSearchDTO dto = new RestaurantSearchDTO();
                    GeoapifyResponse.Properties props = feature.getProperties();

                    dto.setPlaceId(props.getPlace_id());
                    dto.setName(props.getName());
                    dto.setAddress(props.getFormatted());
                    dto.setLatitude(props.getLat());
                    dto.setLongitude(props.getLon());

                    // Extract cuisine
                    if (props.getCategories() != null && props.getCategories().length > 0) {
                        String cuisine = Arrays.stream(props.getCategories())
                                .filter(c -> c.startsWith("catering.restaurant."))
                                .map(c -> c.replace("catering.restaurant.", ""))
                                .findFirst()
                                .orElse("general");
                        dto.setCuisine(cuisine);
                    }

                    // Extract phone and website
                    if (props.getDatasource() != null && props.getDatasource().getRaw() != null) {
                        Map<String, Object> rawData = props.getDatasource().getRaw();

                        // For phone
                        if (rawData.get("phone") instanceof String) {
                            dto.setPhone((String) rawData.get("phone"));
                        }

                        // For website
                        if (rawData.get("website") instanceof String) {
                            dto.setWebsite((String) rawData.get("website"));
                        }
                    }

                    dto.setFromApi(true);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}