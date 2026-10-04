package com.ridelink.drivervehicle.controller;

import com.ridelink.drivervehicle.dto.ApiResponse;
import com.ridelink.drivervehicle.dto.DriverRequest;
import com.ridelink.drivervehicle.model.Driver;
import com.ridelink.drivervehicle.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Tag(name = "Drivers", description = "Driver management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class DriverController {
    
    private final DriverService driverService;
    
    @PostMapping("/profile")
    @Operation(summary = "Create driver profile", description = "Create a new driver profile")
    public ResponseEntity<ApiResponse<Driver>> createDriverProfile(@Valid @RequestBody DriverRequest request) {
        ApiResponse<Driver> response = driverService.createDriverProfile(request);
        return response.isSuccess() ? ResponseEntity.status(201).body(response) : ResponseEntity.badRequest().body(response);
    }
    
    @GetMapping("/profile/{id}")
    @Operation(summary = "Get driver by ID", description = "Retrieve driver profile by ID")
    public ResponseEntity<ApiResponse<Driver>> getDriverById(@PathVariable String id) {
        ApiResponse<Driver> response = driverService.getDriverById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get driver by user ID", description = "Retrieve driver profile by user ID")
    public ResponseEntity<ApiResponse<Driver>> getDriverByUserId(@PathVariable String userId) {
        ApiResponse<Driver> response = driverService.getDriverByUserId(userId);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    
    @GetMapping
    @Operation(summary = "Get all drivers", description = "Retrieve all driver profiles")
    public ResponseEntity<ApiResponse<List<Driver>>> getAllDrivers() {
        ApiResponse<List<Driver>> response = driverService.getAllDrivers();
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/profile/{id}")
    @Operation(summary = "Update driver profile", description = "Update driver profile information")
    public ResponseEntity<ApiResponse<Driver>> updateDriverProfile(@PathVariable String id, @RequestBody DriverRequest request) {
        ApiResponse<Driver> response = driverService.updateDriverProfile(id, request);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/availability/{id}")
    @Operation(summary = "Update driver availability", description = "Update driver availability status")
    public ResponseEntity<ApiResponse<Driver>> updateAvailability(
            @PathVariable String id,
            @RequestParam boolean isAvailable,
            @RequestBody(required = false) DriverRequest.Location location) {
        ApiResponse<Driver> response = driverService.updateAvailability(id, isAvailable, location);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
    
    @PutMapping("/location/{id}")
    @Operation(summary = "Update driver location", description = "Update driver current location")
    public ResponseEntity<ApiResponse<Driver>> updateLocation(@PathVariable String id, @Valid @RequestBody DriverRequest.Location location) {
        ApiResponse<Driver> response = driverService.updateLocation(id, location);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/available")
    @Operation(summary = "Get available drivers", description = "Retrieve available drivers in service area")
    public ResponseEntity<ApiResponse<List<Driver>>> getAvailableDrivers(
        @RequestParam String serviceArea,
        @RequestParam(defaultValue = "5000") double maxDistance,
        @RequestBody double[] coordinates) {
    
    // Ensure coordinates array has [longitude, latitude]
    if (coordinates == null || coordinates.length < 2) {
        return ResponseEntity.badRequest().body(ApiResponse.error("Coordinates must contain [longitude, latitude]"));
    }
    
    ApiResponse<List<Driver>> response = driverService.getAvailableDrivers(serviceArea, coordinates, maxDistance);
    return ResponseEntity.ok(response);
}
    
    @PutMapping("/rating/{id}")
    @Operation(summary = "Update driver rating", description = "Update driver rating after ride completion")
    public ResponseEntity<ApiResponse<Driver>> updateDriverRating(@PathVariable String id, @RequestParam double rating) {
        ApiResponse<Driver> response = driverService.updateDriverRating(id, rating);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
