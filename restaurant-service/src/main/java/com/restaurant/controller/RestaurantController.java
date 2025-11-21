package com.restaurant.controller;

import com.restaurant.entity.Restaurant;
import com.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @PutMapping("/{id}/rating")
    public ResponseEntity<Restaurant> updateRating(@PathVariable Long id, @RequestParam Double rating) {
        Restaurant restaurant = service.updateRating(id, rating);
        return restaurant != null ? ResponseEntity.ok(restaurant) : ResponseEntity.notFound().build();
    }
}