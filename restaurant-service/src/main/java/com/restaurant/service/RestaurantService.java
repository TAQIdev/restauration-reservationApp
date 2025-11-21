package com.restaurant.service;

import com.restaurant.entity.Restaurant;
import com.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository repository;

    public Restaurant saveRestaurant(Restaurant restaurant) {
        return repository.findByPlaceId(restaurant.getPlaceId())
                .orElseGet(() -> repository.save(restaurant));
    }

    public List<Restaurant> getAllRestaurants() {
        return repository.findAll();
    }

    public Restaurant getRestaurantById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Restaurant getRestaurantByPlaceId(String placeId) {
        return repository.findByPlaceId(placeId).orElse(null);
    }

    public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
        return repository.findByCuisineContainingIgnoreCase(cuisine);
    }

    public Restaurant updateRating(Long id, Double newRating) {
        Restaurant restaurant = repository.findById(id).orElse(null);
        if (restaurant != null) {
            int total = restaurant.getTotalReviews() != null ? restaurant.getTotalReviews() : 0;
            double currentRating = restaurant.getRating() != null ? restaurant.getRating() : 0;

            // CORRECTION: Éviter la division par zéro
            double updatedRating;
            if (total == 0) {
                updatedRating = newRating;
            } else {
                updatedRating = ((currentRating * total) + newRating) / (total + 1);
            }

            restaurant.setRating(Math.round(updatedRating * 10.0) / 10.0);
            restaurant.setTotalReviews(total + 1);

            return repository.save(restaurant);
        }
        return null;
    }
}