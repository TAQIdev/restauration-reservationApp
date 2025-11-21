package com.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")

    private Long clientId;

    @Column(name = "restaurant_id")
    private Long restaurantId;

    private String restaurantName;

    @Column(name = "reservation_date")
    private LocalDateTime reservationDate;

    @Column(name = "number_of_people")
    private Integer numberOfPeople;

    private String status;

    @Column(name = "has_review")
    private Boolean hasReview = false; // Track if client has reviewed
}