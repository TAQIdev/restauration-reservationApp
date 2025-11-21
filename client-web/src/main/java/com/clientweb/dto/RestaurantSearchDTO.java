package com.clientweb.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSearchDTO {
    private Long id;
    private String placeId;
    private String name;
    private String address;
    private String cuisine;
    private Double latitude;
    private Double longitude;
    private String phone;
    private String website;
    private Double rating;
    private Integer totalReviews;
    private boolean fromApi; // Indicates if from Geoapify API
}