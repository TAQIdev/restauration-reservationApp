package com.reservation.client;

import com.reservation.dto.RestaurantDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "restaurant-service", url = "${restaurant.service.url}")
public interface RestaurantClient {

    @PostMapping("/api/restaurants")
    RestaurantDTO saveRestaurant(@RequestBody RestaurantDTO restaurant);

    @GetMapping("/api/restaurants/{id}")
    RestaurantDTO getRestaurantById(@PathVariable Long id);

    @PutMapping("/api/restaurants/{id}/rating")
    RestaurantDTO updateRating(@PathVariable Long id, @RequestParam Double rating);
}