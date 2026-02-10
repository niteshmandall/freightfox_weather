package com.freightfox.weather.service;

import com.freightfox.weather.dto.WeatherDTOs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherOrchestrator {

    private final GeocodingService geocodingService;
    private final WeatherService weatherService;

    public WeatherDTOs.WeatherResponse getWeatherInfo(String pincode, LocalDate forDate) {
        log.info("Orchestrating weather request for pincode: {}, date: {}", pincode, forDate);

        // 1. Get Lat/Long
        WeatherDTOs.LatLongDto latLong = geocodingService.getLatLong(pincode);

        // 2. Get Weather
        return weatherService.getWeather(pincode, latLong.getLat(), latLong.getLon(), forDate);
    }
}
