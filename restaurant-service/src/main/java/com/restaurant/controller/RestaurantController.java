package com.restaurant.controller;

import com.restaurant.entity.Restaurant;
import com.restaurant.entity.Review;
import com.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService service;

    @PostMapping
    public ResponseEntity<Restaurant> saveRestaurant(@RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(service.saveRestaurant(restaurant));
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(service.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = service.getRestaurantById(id);
        return restaurant != null ? ResponseEntity.ok(restaurant) : ResponseEntity.notFound().build();
    }

    @GetMapping("/place/{placeId}")
    public ResponseEntity<Restaurant> getRestaurantByPlaceId(@PathVariable String placeId) {
        Restaurant restaurant = service.getRestaurantByPlaceId(placeId);
        return restaurant != null ? ResponseEntity.ok(restaurant) : ResponseEntity.notFound().build();
    }

    @GetMapping("/cuisine/{cuisine}")
    public ResponseEntity<List<Restaurant>> getByCuisine(@PathVariable String cuisine) {
        return ResponseEntity.ok(service.getRestaurantsByCuisine(cuisine));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<Review>> getRestaurantReviews(@PathVariable Long id) {
        return ResponseEntity.ok(service.getRestaurantReviews(id));
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<Review> addReview(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long reservationId = ((Number) body.get("reservationId")).longValue();
        Long clientId = ((Number) body.get("clientId")).longValue();
        String clientName = (String) body.get("clientName");
        Double rating = ((Number) body.get("rating")).doubleValue();
        String comment = (String) body.get("comment");

        Review review = service.addReview(id, reservationId, clientId, clientName, rating, comment);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @RequestParam Long clientId,
            @RequestBody Map<String, Object> body) {
        Double rating = ((Number) body.get("rating")).doubleValue();
        String comment = (String) body.get("comment");

        Review review = service.updateReview(reviewId, clientId, rating, comment);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @RequestParam Long clientId) {
        service.deleteReview(reviewId, clientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reviews/reservation/{reservationId}")
    public ResponseEntity<Review> getReviewByReservation(@PathVariable Long reservationId) {
        Review review = service.getReviewByReservation(reservationId);
        return review != null ? ResponseEntity.ok(review) : ResponseEntity.notFound().build();
    }
}