package com.freightfox.weather.service;

import com.freightfox.weather.dto.WeatherDTOs;
import com.freightfox.weather.model.WeatherLog;
import com.freightfox.weather.repository.WeatherLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

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
    void getWeather_ShouldReturnFromDB_WhenExists() {
        String pincode = "411014";
        LocalDate date = LocalDate.of(2020, 10, 15);
        String json = "{\"weather\":[{\"description\":\"clear sky\"}],\"main\":{\"temp\":25.0,\"humidity\":50},\"wind\":{\"speed\":5.0}}";

        WeatherLog log = WeatherLog.builder()
                .pincode(pincode)
                .forDate(date)
                .weatherJson(json)
                .build();

        when(weatherLogRepository.findByPincodeAndForDate(pincode, date)).thenReturn(Optional.of(log));

        WeatherDTOs.WeatherResponse response = weatherService.getWeather(pincode, 18.5, 73.8, date);

        assertNotNull(response);
        assertEquals("clear sky", response.getWeatherDescription());
        assertEquals(25.0, response.getTemperature());
        Mockito.verify(weatherLogRepository).findByPincodeAndForDate(pincode, date);
    }

    @Test
    void getWeather_ShouldFetchFromApiAndSave_WhenNotExists() {
        String pincode = "411014";
        LocalDate date = LocalDate.now();
        String json = "{\"weather\":[{\"description\":\"clear sky\"}],\"main\":{\"temp\":25.0,\"humidity\":50},\"wind\":{\"speed\":5.0}}";

        when(weatherLogRepository.findByPincodeAndForDate(anyString(), any(LocalDate.class)))
                .thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(json);

        WeatherDTOs.WeatherResponse response = weatherService.getWeather(pincode, 18.5, 73.8, date);

        assertNotNull(response);
        assertEquals(25.0, response.getTemperature());
        Mockito.verify(weatherLogRepository).save(any(WeatherLog.class));
    }
}
