package com.freightfox.weather.service;

import com.freightfox.weather.dto.WeatherDTOs;
import com.freightfox.weather.model.Pincode;
import com.freightfox.weather.repository.PincodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeocodingService {

    private final PincodeRepository pincodeRepository;
    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}") // Note: Geocoding might be a different base URL usually, but we'll adapt
    private String baseUrl;

    // http://api.openweathermap.org/geo/1.0/zip?zip={zip code},{country
    // code}&appid={API key}
    private static final String GEO_URL = "http://api.openweathermap.org/geo/1.0/zip";

    public WeatherDTOs.LatLongDto getLatLong(String pincode) {
        // 1. Check DB
        Optional<Pincode> existing = pincodeRepository.findById(pincode);
        if (existing.isPresent()) {
            log.info("Found lat/long for pincode {} in DB", pincode);
            return WeatherDTOs.LatLongDto.builder()
                    .lat(existing.get().getLatitude())
                    .lon(existing.get().getLongitude())
                    .build();
        }

        // 2. Call API
        log.info("Fetching lat/long for pincode {} from OpenWeatherMap", pincode);
        String url = UriComponentsBuilder.fromHttpUrl(GEO_URL)
                .queryParam("zip", pincode + ",IN") // Assuming India for now based on assignment context (411014)
                .queryParam("appid", apiKey)
                .toUriString();

        try {
            WeatherDTOs.LatLongDto response = restTemplate.getForObject(url, WeatherDTOs.LatLongDto.class);

            if (response != null && response.getLat() != null) {
                // 3. Save to DB
                Pincode newPincode = Pincode.builder()
                        .pincode(pincode)
                        .latitude(response.getLat())
                        .longitude(response.getLon())
                        .build();
                pincodeRepository.save(newPincode);
                return response;
            }
        } catch (Exception e) {
            log.error("Error fetching geocoding data: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch location data for pincode: " + pincode);
        }

        throw new RuntimeException("Location not found for pincode: " + pincode);
    }
}
