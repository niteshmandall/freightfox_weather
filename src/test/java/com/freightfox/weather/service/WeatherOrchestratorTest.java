package com.freightfox.weather.service;

import com.freightfox.weather.dto.WeatherDTOs;
import com.freightfox.weather.model.Pincode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherOrchestratorTest {

    @Mock
    private GeocodingService geocodingService;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherOrchestrator weatherOrchestrator;

    @Test
    void getWeatherInfo_ShouldOrchestrateCallsCorrectly() {
        String pincode = "411014";
        LocalDate date = LocalDate.of(2020, 10, 15);
        WeatherDTOs.LatLongDto latLongDto = WeatherDTOs.LatLongDto.builder().lat(18.5).lon(73.8).build();
        WeatherDTOs.WeatherResponse weatherResponse = WeatherDTOs.WeatherResponse.builder()
                .pincode(pincode)
                .latitude(18.5)
                .longitude(73.8)
                .forDate(date)
                .weatherDescription("clear sky")
                .build();

        when(geocodingService.getLatLong(pincode)).thenReturn(latLongDto);
        when(weatherService.getWeather(pincode, 18.5, 73.8, date)).thenReturn(weatherResponse);

        WeatherDTOs.WeatherResponse result = weatherOrchestrator.getWeatherInfo(pincode, date);

        assertNotNull(result);
        assertEquals("clear sky", result.getWeatherDescription());
        verify(geocodingService).getLatLong(pincode);
        verify(weatherService).getWeather(pincode, 18.5, 73.8, date);
    }
}
