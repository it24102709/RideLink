# VS Code Setup for Spring Boot

## Required Extensions

Install these extensions in VS Code:

1. **Extension Pack for Java** (by Microsoft)
   - Includes: Language Support for Java, Debugger for Java, Maven for Java
   
2. **Spring Boot Extension Pack** (by Pivotal)
   - Includes: Spring Boot Tools, Spring Initializr, Spring Dashboard

## Running the Application

### Option 1: Using VS Code (Recommended)

1. Open the project in VS Code
2. Install the required extensions above
3. Open `src/main/java/com/ridelink/drivervehicle/DriverVehicleServiceApplication.java`
4. Click the "Run" button above the main method
5. Or press `F5` to debug

### Option 2: Using Maven (if installed)

1. Open terminal in VS Code
2. Run:
```bash
mvn spring-boot:run
```

### Option 3: Using JAR file

1. Build the project:
```bash
mvn clean package
```

2. Run the JAR:
```bash
java -jar target/driver-vehicle-service-1.0.0.jar
```

## Prerequisites

1. **Java 17** must be installed
   - Download from: https://www.oracle.com/java/technologies/downloads/#java17
   - Set JAVA_HOME environment variable

2. **MongoDB** must be running
   - Local: `mongodb://localhost:27017`
   - Or update `application.properties` with your MongoDB URI

3. **Maven** (optional, for Option 2)
   - Download from: https://maven.apache.org/download.cgi
   - Add to PATH

## Accessing the Application

Once running:
- **API**: http://localhost:3002
- **Swagger UI**: http://localhost:3002/swagger-ui.html
- **Health Check**: http://localhost:3002/health

## Troubleshooting

### "java is not recognized"
- Install Java 17
- Set JAVA_HOME environment variable
- Restart VS Code

### "mvn is not recognized"
- Install Maven
- Add Maven to PATH
- Restart VS Code
- Or use VS Code Java extensions (Option 1)

### MongoDB connection failed
- Ensure MongoDB is running
- Check connection string in `application.properties`
- Verify MongoDB is accessible on localhost:27017

## Assignment Compliance

This project is fully compliant with IT3130 assignment requirements:
- ✅ Java 17 + Spring Boot 3.2.0
- ✅ MongoDB database
- ✅ Swagger UI for API documentation
- ✅ JWT authentication
- ✅ RESTful APIs
- ✅ No MERN stack (Node.js/Express not used)

**Important**: Delete the old `driver-vehicle-service` (Node.js version) folder as it's non-compliant and will result in zero marks.
