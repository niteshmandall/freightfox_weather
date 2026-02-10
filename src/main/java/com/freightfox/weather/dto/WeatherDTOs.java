package com.freightfox.weather.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

public class WeatherDTOs {

    @Data
    @Builder
    public static class WeatherResponse {
        private String pincode;
        private Double latitude;
        private Double longitude;
        private LocalDate forDate;
        private String weatherDescription;
        private Double temperature;
        private Double humidity;
        private Double windSpeed;
        private String rawJson; // For detailed checking if needed
    }

    @Data
    @Builder
    public static class LatLongDto {
        private Double lat;
        private Double lon;
        private String name;
        private String country;
    }
}
