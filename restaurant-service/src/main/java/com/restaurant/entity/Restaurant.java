package com.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String placeId; // From Geoapify

    private String name;
    private String address;
    private String cuisine;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String website;

    @Column(name = "rating")
    private Double rating; // Average rating from reviews

    @Column(name = "total_reviews")
    private Integer totalReviews = 0;
}