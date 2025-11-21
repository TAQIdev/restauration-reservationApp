package com.reservation.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {
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
}