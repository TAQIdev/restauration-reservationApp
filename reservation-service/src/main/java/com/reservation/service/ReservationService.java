package com.reservation.service;

import com.reservation.client.RestaurantClient;
import com.reservation.dto.RestaurantDTO;
import com.reservation.dto.ReviewDTO;
import com.reservation.entity.Client;
import com.reservation.entity.Reservation;
import com.reservation.repository.ClientRepository;
import com.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository repository;
    private final ClientRepository clientRepository;
    private final RestaurantClient restaurantClient;

    @Transactional
    public Reservation createReservation(Reservation reservation, RestaurantDTO restaurantDTO) {
        // Get client entity
        Client client = clientRepository.findById(reservation.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Save restaurant to database if it doesn't exist
        RestaurantDTO savedRestaurant = restaurantClient.saveRestaurant(restaurantDTO);

        reservation.setClient(client);
        reservation.setRestaurantId(savedRestaurant.getId());
        reservation.setRestaurantName(savedRestaurant.getName());
        reservation.setStatus("CONFIRMED");
        reservation.setHasReview(false);

        return repository.save(reservation);
    }

    public List<Reservation> getClientReservations(Long clientId) {
        return repository.findByClientId(clientId);
    }

    @Transactional
    public Reservation updateReservation(Long id, Reservation reservation) {
        Reservation existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // Mettre à jour la date de réservation si fournie
        if (reservation.getReservationDate() != null) {
            existing.setReservationDate(reservation.getReservationDate());
        }

        // Mettre à jour le nombre de personnes si fourni
        if (reservation.getNumberOfPeople() != null) {
            existing.setNumberOfPeople(reservation.getNumberOfPeople());
        }

        // Mettre à jour le statut si fourni
        if (reservation.getStatus() != null) {
            existing.setStatus(reservation.getStatus());
        }

        return repository.save(existing);
    }

    @Transactional
    public void deleteReservation(Long id) {
        repository.deleteById(id);
    }

    // Review Management
    @Transactional
    public ReviewDTO addReviewToReservation(Long reservationId, Long clientId, String clientName,
                                            Double rating, String comment) {
        Reservation reservation = repository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getClient().getId().equals(clientId)) {
            throw new RuntimeException("Unauthorized: Not your reservation");
        }

        if (reservation.getHasReview()) {
            throw new RuntimeException("Review already exists for this reservation");
        }

        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("reservationId", reservationId);
        reviewData.put("clientId", clientId);
        reviewData.put("clientName", clientName);
        reviewData.put("rating", rating);
        reviewData.put("comment", comment);

        ReviewDTO review = restaurantClient.addReview(reservation.getRestaurantId(), reviewData);

        // Mark reservation as reviewed
        reservation.setHasReview(true);
        repository.save(reservation);

        return review;
    }

    @Transactional
    public ReviewDTO updateReview(Long reviewId, Long clientId, Double rating, String comment) {
        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("rating", rating);
        reviewData.put("comment", comment);

        return restaurantClient.updateReview(reviewId, clientId, reviewData);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long reservationId, Long clientId) {
        Reservation reservation = repository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        restaurantClient.deleteReview(reviewId, clientId);

        // Mark reservation as not reviewed
        reservation.setHasReview(false);
        repository.save(reservation);
    }

    public ReviewDTO getReviewByReservation(Long reservationId) {
        return restaurantClient.getReviewByReservation(reservationId);
    }
}