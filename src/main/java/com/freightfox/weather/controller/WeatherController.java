package com.freightfox.weather.controller;

import com.freightfox.weather.dto.WeatherDTOs;
import com.freightfox.weather.service.WeatherOrchestrator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Validated
@Tag(name = "Weather", description = "Weather Information API")
public class WeatherController {

    private final WeatherOrchestrator weatherOrchestrator;

    @GetMapping
    @Operation(summary = "Get Weather Info", description = "Retrieve weather information for a specific pincode and date.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved weather information"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request parameters (e.g., missing pincode or date)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Weather data not found for the given location/date"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<WeatherDTOs.WeatherResponse> getWeather(
            @RequestParam(name = "pincode") String pincode,
            @RequestParam(name = "for_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate forDate) {

        // Basic validation can be added here or via @Pattern on params if needed (e.g.
        // 6 digit pincode)

        WeatherDTOs.WeatherResponse response = weatherOrchestrator.getWeatherInfo(pincode, forDate);
        return ResponseEntity.ok(response);
    }
}
