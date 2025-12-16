package com.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_id", unique = true)
    private String placeId; // From Geoapify

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
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

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}