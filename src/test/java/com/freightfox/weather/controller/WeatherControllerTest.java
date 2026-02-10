package com.freightfox.weather.controller;

import com.freightfox.weather.dto.WeatherDTOs;
import com.freightfox.weather.service.WeatherOrchestrator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherOrchestrator weatherOrchestrator;

    @Test
    void getWeather_ShouldReturnBadRequest_WhenPincodeIsMissing() throws Exception {
        mockMvc.perform(get("/api/weather")
                .param("for_date", "2020-10-15")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));
    }

    @Test
    void getWeather_ShouldReturnBadRequest_WhenDateIsMissing() throws Exception {
        mockMvc.perform(get("/api/weather")
                .param("pincode", "411014")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));
    }

    @Test
    void getWeather_ShouldReturnWeatherInfo() throws Exception {
        LocalDate date = LocalDate.of(2020, 10, 15);
        WeatherDTOs.WeatherResponse response = WeatherDTOs.WeatherResponse.builder()
                .pincode("411014")
                .latitude(18.5)
                .longitude(73.8)
                .forDate(date)
                .weatherDescription("clear sky")
                .temperature(25.0)
                .humidity(50.0)
                .windSpeed(5.0)
                .build();

        when(weatherOrchestrator.getWeatherInfo(eq("411014"), eq(date))).thenReturn(response);

        mockMvc.perform(get("/api/weather")
                .param("pincode", "411014")
                .param("for_date", "2020-10-15")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pincode").value("411014"))
                .andExpect(jsonPath("$.weatherDescription").value("clear sky"));
    }
}
