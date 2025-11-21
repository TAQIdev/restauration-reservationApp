package com.clientweb.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoapifyResponse {
    private String type;
    private List<Feature> features;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Feature {
        private String type;
        private Properties properties;
        private Geometry geometry;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Properties {
        private String name;
        private String formatted;
        private String address_line1;
        private String address_line2;
        private String[] categories;
        private Double lat;
        private Double lon;
        private String place_id;
        private Datasource datasource;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Datasource {
        private String sourcename;
        private Map<String, Object> raw;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Geometry {
        private String type;
        private double[] coordinates;
    }
}