package com.reservation.service;

import com.reservation.client.RestaurantClient;
import com.reservation.dto.RestaurantDTO;
import com.reservation.entity.Reservation;
import com.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository repository;
    private final RestaurantClient restaurantClient;

    public Reservation createReservation(Reservation reservation, RestaurantDTO restaurantDTO) {
        // Save restaurant to database if it doesn't exist
        RestaurantDTO savedRestaurant = restaurantClient.saveRestaurant(restaurantDTO);

        reservation.setRestaurantId(savedRestaurant.getId());
        reservation.setRestaurantName(savedRestaurant.getName());
        reservation.setStatus("CONFIRMED");

        return repository.save(reservation);
    }

    public List<Reservation> getClientReservations(Long clientId) {
        return repository.findByClientId(clientId);
    }

    public Reservation updateReservation(Long id, Reservation reservation) {
        Reservation existing = repository.findById(id).orElse(null);
        if (existing != null) {
            if (reservation.getRating() != null) {
                existing.setRating(reservation.getRating());
                // Update restaurant rating
                restaurantClient.updateRating(existing.getRestaurantId(), reservation.getRating());
            }
            if (reservation.getComment() != null) {
                existing.setComment(reservation.getComment());
            }
            if (reservation.getStatus() != null) {
                existing.setStatus(reservation.getStatus());
            }
            return repository.save(existing);
        }
        return null;
    }

    public void deleteReservation(Long id) {
        repository.deleteById(id);
    }
}