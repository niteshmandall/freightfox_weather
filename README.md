# Weather Info Service

## Overview
This is a Spring Boot application that provides a REST API to retrieve weather information for a specific pincode and date. It integrates with OpenWeatherMap API and caches the results (Geocoding and Weather data) in an H2 in-memory database to optimize API calls.

## Features
- **Get Weather by Pincode & Date**: Retrieves weather info.
- **Geocoding Caching**: Caches Lat/Long for pincodes to avoid repeated API calls.
- **Weather Data Caching**: Caches weather data for a specific date and pincode.
- **Swagger UI**: Interactive API documentation.
- **H2 Database**: In-memory database for storage.

## Tech Stack
- Java 17
- Spring Boot 3.4.2
- Spring Data JPA (Hibernate)
- H2 Database
- Lombok
- OpenWeatherMap API
- JUnit 5 & Mockito (Testing)

## Prerequisites
- Java 17+ installed
- Maven installed (or use provided `mvnw` wrapper)
- OpenWeatherMap API Key

## Configuration
Update `src/main/resources/application.properties` with your API keys:

```properties
weather.api.key=YOUR_OPENWEATHER_API_KEY
```

## Build and Run

1.  **Clone the repository**
2.  **Build the project:**
    ```bash
    ./mvnw clean install
    ```
3.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```

The application will start on `http://localhost:8080`.

## API Documentation
Once the application is running, access Swagger UI at:
- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Endpoint
**GET** `/api/weather`

**Parameters:**
- `pincode`: (String) e.g., `411014`
- `for_date`: (Date `YYYY-MM-DD`) e.g., `2020-10-15`

**Example Request:**
`GET http://localhost:8080/api/weather?pincode=411014&for_date=2020-10-15`

**Example Response:**
```json
{
  "pincode": "411014",
  "latitude": 18.5,
  "longitude": 73.8,
  "forDate": "2020-10-15",
  "weatherDescription": "clear sky",
  "temperature": 25.0,
  "humidity": 50.0,
  "windSpeed": 5.0,
  "rawJson": "..."
}
```

## Database Console
Access H2 Console at:
- [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL**: `jdbc:h2:mem:weatherdb`
- **User**: `sa`
- **Password**: `password`

## Testing
Run unit and integration tests using:
```bash
./mvnw test
```
