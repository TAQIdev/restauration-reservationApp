package com.clientweb.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    private String cuisine;
    private Double latitude;
    private Double longitude;
    private Double rating;
    private Integer availableTables;
    private Double distance;
}