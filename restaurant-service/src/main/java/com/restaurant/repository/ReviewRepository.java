package com.restaurant.repository;

import com.restaurant.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRestaurantId(Long restaurantId);
    Optional<Review> findByReservationId(Long reservationId);
    Optional<Review> findByIdAndClientId(Long id, Long clientId);
    void deleteByReservationId(Long reservationId);
}