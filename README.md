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
The application uses environment variables for sensitive keys. You can set them in your IDE (IntelliJ/Eclipse) or terminal.

**Required Environment Variables:**
- `OPENWEATHER_API_KEY`: Your OpenWeatherMap API Key.

**Example (Terminal):**
```bash
export OPENWEATHER_API_KEY=your_actual_key_here
./mvnw spring-boot:run
```

**Example (Powershell):**
```powershell
$env:OPENWEATHER_API_KEY="your_actual_key_here"
./mvnw spring-boot:run
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
- `pincode`: (String) e.g., `411014` (Required)
- `for_date`: (Date `YYYY-MM-DD`) e.g., `2020-10-15` (Required)

**Example Request:**
`GET http://localhost:8080/api/weather?pincode=411014&for_date=2020-10-15`

**Success Response (200 OK):**
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

**Error Response (400 Bad Request):**
```json
{
  "timestamp": "2026-02-11T01:30:00",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Required request parameter 'pincode' for method parameter type String is not present"
}
```
**Validation Error Response (400 Bad Request):**
```json
{
  "timestamp": "2026-02-11T01:30:00",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Validation failed",
  "details": {
    "pincode": "must match \"^\\d{6}$\""
  }
}
```

## Database Console
Access H2 Console at:
- [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL**: `jdbc:h2:mem:weatherdb`
- **User**: `sa`
- **Password**: `password`

## Testing
The application includes a comprehensive test suite covering unit, integration, and edge-case scenarios.

**Run All Tests (Deep Testing):**
```bash
./mvnw clean test
```

**What is Tested:**
1.  **Service Layer**: Mockito tests verify caching logic (DB vs API) and error handling.
2.  **Controller Layer**: Integration tests verify API endpoints, including validation for missing parameters.
3.  **Orchestrator**: Verifies coordination between Geocoding and Weather services.

**Manual Verification (Postman):**
1.  Start the app: `./mvnw spring-boot:run`
2.  Import the collection or make a GET request to `http://localhost:8080/api/weather`.
3.  Test with valid data (`pincode=411014`, `for_date=2020-10-15`).
4.  Test with missing parameters to see standardized error responses.
