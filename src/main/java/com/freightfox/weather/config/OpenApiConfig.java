package com.freightfox.weather.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI weatherOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Weather Info API")
                        .description("API to retrieve weather information for a specific pincode and date.")
                        .version("1.0"));
    }
}
