package com.freightfox.weather.repository;

import com.freightfox.weather.model.WeatherLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WeatherLogRepository extends JpaRepository<WeatherLog, Long> {
    Optional<WeatherLog> findByPincodeAndForDate(String pincode, LocalDate forDate);
}
