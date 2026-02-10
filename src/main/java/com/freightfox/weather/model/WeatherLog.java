package com.freightfox.weather.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "weather_log", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "pincode", "forDate" })
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pincode;

    @Column(nullable = false)
    private LocalDate forDate;

    @Column(columnDefinition = "TEXT") // Store JSON as text or generic info
    private String weatherJson;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
