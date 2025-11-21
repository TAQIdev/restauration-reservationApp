package com.restaurant.service;

import com.restaurant.entity.Restaurant;
import com.restaurant.entity.Review;
import com.restaurant.repository.RestaurantRepository;
import com.restaurant.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.findByPlaceId(restaurant.getPlaceId())
                .orElseGet(() -> restaurantRepository.save(restaurant));
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    public Restaurant getRestaurantByPlaceId(String placeId) {
        return restaurantRepository.findByPlaceId(placeId).orElse(null);
    }

    public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
        return restaurantRepository.findByCuisineContainingIgnoreCase(cuisine);
    }

    @Transactional
    public Review addReview(Long restaurantId, Long reservationId, Long clientId,
                            String clientName, Double rating, String comment) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Check if review already exists for this reservation
        if (reviewRepository.findByReservationId(reservationId).isPresent()) {
            throw new RuntimeException("Review already exists for this reservation");
        }

        Review review = new Review();
        review.setClientId(clientId);
        review.setClientName(clientName);
        review.setRestaurant(restaurant);
        review.setReservationId(reservationId);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        // Update restaurant rating
        recalculateRestaurantRating(restaurantId);

        return savedReview;
    }

    @Transactional
    public Review updateReview(Long reviewId, Long clientId, Double rating, String comment) {
        Review review = reviewRepository.findByIdAndClientId(reviewId, clientId)
                .orElseThrow(() -> new RuntimeException("Review not found or unauthorized"));

        review.setRating(rating);
        review.setComment(comment);
        review.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(review);

        // Recalculate restaurant rating
        recalculateRestaurantRating(review.getRestaurant().getId());

        return updatedReview;
    }

    @Transactional
    public void deleteReview(Long reviewId, Long clientId) {
        Review review = reviewRepository.findByIdAndClientId(reviewId, clientId)
                .orElseThrow(() -> new RuntimeException("Review not found or unauthorized"));

        Long restaurantId = review.getRestaurant().getId();
        reviewRepository.delete(review);

        // Recalculate restaurant rating
        recalculateRestaurantRating(restaurantId);
    }

    public List<Review> getRestaurantReviews(Long restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId);
    }

    public Review getReviewByReservation(Long reservationId) {
        return reviewRepository.findByReservationId(reservationId).orElse(null);
    }

    private void recalculateRestaurantRating(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant != null) {
            List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);

            if (reviews.isEmpty()) {
                restaurant.setRating(null);
                restaurant.setTotalReviews(0);
            } else {
                double avgRating = reviews.stream()
                        .mapToDouble(Review::getRating)
                        .average()
                        .orElse(0.0);

                restaurant.setRating(Math.round(avgRating * 10.0) / 10.0);
                restaurant.setTotalReviews(reviews.size());
            }

            restaurantRepository.save(restaurant);
        }
    }
}