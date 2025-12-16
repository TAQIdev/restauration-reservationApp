package com.reservation.controller;

import com.reservation.dto.RestaurantDTO;
import com.reservation.dto.ReviewDTO;
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
            @RequestBody Map<String, Object> requestBody) {

        Reservation reservation = new Reservation();

        // Mettre à jour la date si fournie
        if (requestBody.containsKey("reservationDate")) {
            String dateStr = (String) requestBody.get("reservationDate");
            reservation.setReservationDate(java.time.LocalDateTime.parse(dateStr));
        }

        // Mettre à jour le nombre de personnes si fourni
        if (requestBody.containsKey("numberOfPeople")) {
            reservation.setNumberOfPeople(((Number) requestBody.get("numberOfPeople")).intValue());
        }

        // Mettre à jour le statut si fourni
        if (requestBody.containsKey("status")) {
            reservation.setStatus((String) requestBody.get("status"));
        }

        Reservation updated = service.updateReservation(id, reservation);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        service.deleteReservation(id);
        return ResponseEntity.ok().build();
    }

    // Review endpoints
    @PostMapping("/{reservationId}/review")
    public ResponseEntity<ReviewDTO> addReview(
            @PathVariable Long reservationId,
            @RequestBody Map<String, Object> body) {

        Long clientId = ((Number) body.get("clientId")).longValue();
        String clientName = (String) body.get("clientName");
        Double rating = ((Number) body.get("rating")).doubleValue();
        String comment = (String) body.get("comment");

        ReviewDTO review = service.addReviewToReservation(reservationId, clientId, clientName, rating, comment);
        return ResponseEntity.ok(review);
    }

    @PutMapping("/{reservationId}/review/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long reservationId,
            @PathVariable Long reviewId,
            @RequestBody Map<String, Object> body) {

        Long clientId = ((Number) body.get("clientId")).longValue();
        Double rating = ((Number) body.get("rating")).doubleValue();
        String comment = (String) body.get("comment");

        ReviewDTO review = service.updateReview(reviewId, clientId, rating, comment);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{reservationId}/review/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reservationId,
            @PathVariable Long reviewId,
            @RequestParam Long clientId) {

        service.deleteReview(reviewId, reservationId, clientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{reservationId}/review")
    public ResponseEntity<ReviewDTO> getReviewByReservation(@PathVariable Long reservationId) {
        ReviewDTO review = service.getReviewByReservation(reservationId);
        return review != null ? ResponseEntity.ok(review) : ResponseEntity.notFound().build();
    }
}