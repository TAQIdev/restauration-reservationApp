package com.reservation.client;

import com.reservation.dto.RestaurantDTO;
import com.reservation.dto.ReviewDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@FeignClient(name = "restaurant-service", url = "${restaurant.service.url}")
public interface RestaurantClient {

    @PostMapping("/api/restaurants")
    RestaurantDTO saveRestaurant(@RequestBody RestaurantDTO restaurant);

    @GetMapping("/api/restaurants/{id}")
    RestaurantDTO getRestaurantById(@PathVariable Long id);

    @PostMapping("/api/restaurants/{id}/reviews")
    ReviewDTO addReview(@PathVariable Long id, @RequestBody Map<String, Object> body);

    @PutMapping("/api/restaurants/reviews/{reviewId}")
    ReviewDTO updateReview(@PathVariable Long reviewId, @RequestParam Long clientId, @RequestBody Map<String, Object> body);

    @DeleteMapping("/api/restaurants/reviews/{reviewId}")
    void deleteReview(@PathVariable Long reviewId, @RequestParam Long clientId);

    @GetMapping("/api/restaurants/reviews/reservation/{reservationId}")
    ReviewDTO getReviewByReservation(@PathVariable Long reservationId);
}