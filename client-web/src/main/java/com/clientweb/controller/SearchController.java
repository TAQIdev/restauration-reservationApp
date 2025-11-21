package com.clientweb.controller;

import com.clientweb.dto.RestaurantSearchDTO;
import com.clientweb.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantSearchDTO>> searchRestaurants(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(required = false) String cuisine,
            @RequestParam(defaultValue = "5000") int radius) {

        List<RestaurantSearchDTO> results = searchService.searchRestaurants(lat, lon, cuisine, radius);
        return ResponseEntity.ok(results);
    }
}