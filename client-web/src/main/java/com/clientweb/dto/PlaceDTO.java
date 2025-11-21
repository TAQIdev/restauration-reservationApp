package com.clientweb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceDTO {
    private String type;
    private Properties properties;
    private Geometry geometry;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Properties {
        private String name;
        private String formatted;
        private String address_line1;
        private String address_line2;
        private String city;
        private String country;
        private String[] categories;
        private Double lat;
        private Double lon;
        private Double distance;
        private String cuisine;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Geometry {
        private String type;
        private double[] coordinates;
    }
}