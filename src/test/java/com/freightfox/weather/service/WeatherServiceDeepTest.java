package com.freightfox.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freightfox.weather.model.WeatherLog;
import com.freightfox.weather.repository.WeatherLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceDeepTest {

    @Mock
    private WeatherLogRepository weatherLogRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(weatherService, "apiKey", "test-key");
    }

    @Test
    void getWeather_ShouldThrowException_WhenApiCallFails() {
        String pincode = "411014";
        LocalDate date = LocalDate.now();

        when(weatherLogRepository.findByPincodeAndForDate(pincode, date)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException("API Error"));

        assertThrows(RuntimeException.class, () -> {
            weatherService.getWeather(pincode, 18.5, 73.8, date);
        });
    }

    @Test
    void getWeather_ShouldThrowException_WhenJsonParsingFails() throws JsonProcessingException {
        // This test is tricky because ObjectMapper is final in Service.
        // We can simulate parsing error by providing invalid JSON from "API" response
        // mock
        // BUT wait, ObjectMapper.readTree might throw JsonProcessingException.
        // Let's rely on standard parsing error.

        String pincode = "411014";
        LocalDate date = LocalDate.of(2020, 10, 15);
        String invalidJson = "{ invalid json }";

        WeatherLog log = WeatherLog.builder()
                .pincode(pincode)
                .forDate(date)
                .weatherJson(invalidJson)
                .build();

        when(weatherLogRepository.findByPincodeAndForDate(pincode, date)).thenReturn(Optional.of(log));

        // The service uses "new ObjectMapper()" internally so we can't mock it easily
        // without refactoring.
        // However, passing invalid JSON to valid ObjectMapper will cause readTree to
        // throw or return null/error.
        // Actually readTree throws JsonProcessingException for strictly invalid JSON.

        assertThrows(RuntimeException.class, () -> {
            weatherService.getWeather(pincode, 18.5, 73.8, date);
        });
    }
}
