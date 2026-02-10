package com.freightfox.weather.service;

import com.freightfox.weather.dto.WeatherDTOs;
import com.freightfox.weather.model.Pincode;
import com.freightfox.weather.repository.PincodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeocodingServiceTest {

    @Mock
    private PincodeRepository pincodeRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeocodingService geocodingService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(geocodingService, "apiKey", "test-key");
        ReflectionTestUtils.setField(geocodingService, "baseUrl", "http://test-url");
    }

    @Test
    void getLatLong_ShouldReturnFromDB_WhenExists() {
        String pincode = "411014";
        Pincode existingPincode = Pincode.builder()
                .pincode(pincode)
                .latitude(18.5)
                .longitude(73.8)
                .build();

        when(pincodeRepository.findById(pincode)).thenReturn(Optional.of(existingPincode));

        WeatherDTOs.LatLongDto result = geocodingService.getLatLong(pincode);

        assertNotNull(result);
        assertEquals(18.5, result.getLat());
        assertEquals(73.8, result.getLon());
        Mockito.verify(pincodeRepository).findById(pincode);
    }

    @Test
    void getLatLong_ShouldFetchFromApiAndSave_WhenNotExists() {
        String pincode = "411014";
        WeatherDTOs.LatLongDto apiResponse = WeatherDTOs.LatLongDto.builder()
                .lat(18.5)
                .lon(73.8)
                .build();

        when(pincodeRepository.findById(pincode)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(), eq(WeatherDTOs.LatLongDto.class)))
                .thenReturn(apiResponse);

        WeatherDTOs.LatLongDto result = geocodingService.getLatLong(pincode);

        assertNotNull(result);
        assertEquals(18.5, result.getLat());
        Mockito.verify(pincodeRepository).save(any(Pincode.class));
    }
}
