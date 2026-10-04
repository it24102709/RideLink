# Driver & Vehicle Service

Backend microservice for managing driver profiles and vehicle information in the RideLink ride-sharing platform.

## Technology Stack

- **Runtime**: Java 17
- **Framework**: Spring Boot 3.2.0
- **Database**: MongoDB with Spring Data MongoDB
- **Authentication**: JWT with Spring Security
- **API Documentation**: Swagger/OpenAPI with SpringDoc
- **Build Tool**: Maven

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MongoDB 4.4 or higher

## Installation

1. Clone the repository and navigate to the service directory:
```bash
cd driver-vehicle-service-springboot
```

2. Install dependencies:
```bash
mvn clean install
```

3. Configure the `application.properties` file with your settings:
```properties
server.port=3002
spring.data.mongodb.uri=mongodb://localhost:27017/ridelink_driver_vehicle
jwt.secret=your_jwt_secret_key_here_change_in_production
jwt.expiration=3600000
account.service.url=http://localhost:3001
```

## Running the Service

### Development Mode
```bash
mvn spring-boot:run
```

### Production Mode
```bash
mvn clean package
java -jar target/driver-vehicle-service-1.0.0.jar
```

The service will start on port 3002 (or the port specified in application.properties).

### Accessing API Documentation
Once the service is running, access Swagger UI at:
```
http://localhost:3002/swagger-ui.html
```

## API Endpoints

### Driver Endpoints

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| POST | `/api/drivers/profile` | Create driver profile | Driver/Admin |
| GET | `/api/drivers/profile/{id}` | Get driver by ID | All authenticated |
| GET | `/api/drivers/user/{userId}` | Get driver by user ID | All authenticated |
| PUT | `/api/drivers/profile/{id}` | Update driver profile | Driver/Admin |
| PUT | `/api/drivers/availability/{id}` | Update availability status | Driver/Admin |
| PUT | `/api/drivers/location/{id}` | Update current location | Driver/Admin |
| POST | `/api/drivers/available` | Get available drivers | Admin |
| PUT | `/api/drivers/rating/{id}` | Update driver rating | Admin |

### Vehicle Endpoints

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| POST | `/api/vehicles/register` | Register vehicle | Driver/Admin |
| GET | `/api/vehicles/{id}` | Get vehicle by ID | All authenticated |
| GET | `/api/vehicles/driver/{driverId}` | Get vehicle by driver ID | All authenticated |
| PUT | `/api/vehicles/{id}` | Update vehicle details | Driver/Admin |
| PUT | `/api/vehicles/{id}/status` | Update vehicle status | Driver/Admin |
| DELETE | `/api/vehicles/{id}` | Delete vehicle | Driver/Admin |

## Sample API Usage

### Create Driver Profile
```bash
POST /api/drivers/profile
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "userId": "507f1f77bcf86cd799439011",
  "licenseNumber": "DL12345",
  "licenseExpiryDate": "2025-12-31T00:00:00",
  "serviceArea": "Colombo"
}
```

### Register Vehicle
```bash
POST /api/vehicles/register
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "driverId": "<driver_id>",
  "make": "Toyota",
  "model": "Corolla",
  "year": 2020,
  "licensePlate": "ABC-1234",
  "color": "White",
  "vehicleType": "sedan",
  "capacity": 4,
  "registrationExpiryDate": "2025-12-31T00:00:00",
  "insuranceExpiryDate": "2025-12-31T00:00:00"
}
```

### Update Availability
```bash
PUT /api/drivers/availability/{id}?isAvailable=true
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "coordinates": [79.8657, 6.9271],
  "locationName": "Colombo City Center"
}
```

## Data Models

### Driver Model
```java
{
  "id": "String",
  "userId": "String",           // Reference to Account Service
  "licenseNumber": "String",       // Unique
  "licenseExpiryDate": "LocalDateTime",
  "isAvailable": "boolean",        // Default: false
  "currentLocation": {
    "type": "Point",
    "coordinates": [double],    // [longitude, latitude]
    "locationName": "String"
  },
  "serviceArea": "String",
  "rating": {
    "average": "double",           // 0-5
    "totalRides": "int"
  },
  "status": "String",             // active, inactive, suspended
  "vehicle": "Vehicle"            // Reference to Vehicle
}
```

### Vehicle Model
```java
{
  "id": "String",
  "driverId": "String",         // Reference to Driver
  "make": "String",
  "model": "String",
  "year": "int",
  "licensePlate": "String",       // Unique
  "color": "String",
  "vehicleType": "String",        // sedan, suv, hatchback, van, luxury
  "capacity": "int",           // 1-8 passengers
  "registrationExpiryDate": "LocalDateTime",
  "insuranceExpiryDate": "LocalDateTime",
  "status": "String"              // active, inactive, maintenance
}
```

## Security Features

- JWT-based authentication
- Role-based authorization (driver, admin)
- Input validation
- Spring Security configuration
- No secrets committed to repository (use environment variables)

## Project Structure

```
driver-vehicle-service-springboot/
├── src/
│   ├── main/
│   │   ├── java/com/ridelink/drivervehicle/
│   │   │   ├── config/           # Configuration classes
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── model/            # Entity models
│   │   │   ├── repository/       # Spring Data repositories
│   │   │   ├── security/         # Security configuration
│   │   │   ├── service/          # Business logic
│   │   │   └── DriverVehicleServiceApplication.java
│   │   └── resources/
│   │       └── application.properties
└── pom.xml
```

## Testing

Run unit tests:
```bash
mvn test
```

## Assignment Compliance

This service is developed using:
- **Java 17** and **Spring Boot 3.2.0** (compliant with assignment requirements)
- **MongoDB** as the database
- **Swagger UI** for API documentation
- **JWT** for authentication
- RESTful APIs for interservice communication

## Service Dependencies

- **Account Service**: For user authentication and validation
- **MongoDB**: For data persistence
