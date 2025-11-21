package com.reservation.controller;

import com.reservation.dto.RestaurantDTO;
import com.reservation.entity.Reservation;
import com.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService service;

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> requestBody) {

        Reservation reservation = new Reservation();
        reservation.setClientId(((Number) requestBody.get("clientId")).longValue());
        reservation.setReservationDate(
                java.time.LocalDateTime.parse((String) requestBody.get("reservationDate"))
        );
        reservation.setNumberOfPeople(((Number) requestBody.get("numberOfPeople")).intValue());

        // Extract restaurant info
        @SuppressWarnings("unchecked")
        Map<String, Object> restaurantData = (Map<String, Object>) requestBody.get("restaurant");

        RestaurantDTO restaurantDTO = new RestaurantDTO();
        restaurantDTO.setPlaceId((String) restaurantData.get("placeId"));
        restaurantDTO.setName((String) restaurantData.get("name"));
        restaurantDTO.setAddress((String) restaurantData.get("address"));
        restaurantDTO.setCuisine((String) restaurantData.get("cuisine"));
        restaurantDTO.setLatitude(((Number) restaurantData.get("latitude")).doubleValue());
        restaurantDTO.setLongitude(((Number) restaurantData.get("longitude")).doubleValue());
        restaurantDTO.setPhone((String) restaurantData.get("phone"));
        restaurantDTO.setWebsite((String) restaurantData.get("website"));

        return ResponseEntity.ok(service.createReservation(reservation, restaurantDTO));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Reservation>> getClientReservations(@PathVariable Long clientId) {
        return ResponseEntity.ok(service.getClientReservations(clientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long id,
            @RequestBody Reservation reservation) {
        Reservation updated = service.updateReservation(id, reservation);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        service.deleteReservation(id);
        return ResponseEntity.ok().build();
    }
}