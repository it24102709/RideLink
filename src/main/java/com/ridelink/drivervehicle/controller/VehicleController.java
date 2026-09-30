package com.ridelink.drivervehicle.controller;

import com.ridelink.drivervehicle.dto.ApiResponse;
import com.ridelink.drivervehicle.dto.VehicleRequest;
import com.ridelink.drivervehicle.model.Vehicle;
import com.ridelink.drivervehicle.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles", description = "Vehicle management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {
    
    private final VehicleService vehicleService;
    
    @PostMapping("/register")
    @Operation(summary = "Register vehicle", description = "Register a new vehicle for a driver")
    public ResponseEntity<ApiResponse<Vehicle>> registerVehicle(@Valid @RequestBody VehicleRequest request) {
        ApiResponse<Vehicle> response = vehicleService.registerVehicle(request);
        return response.isSuccess() ? ResponseEntity.status(201).body(response) : ResponseEntity.badRequest().body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID", description = "Retrieve vehicle by ID")
    public ResponseEntity<ApiResponse<Vehicle>> getVehicleById(@PathVariable String id) {
        ApiResponse<Vehicle> response = vehicleService.getVehicleById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get vehicle by driver ID", description = "Retrieve vehicle by driver ID")
    public ResponseEntity<ApiResponse<Vehicle>> getVehicleByDriverId(@PathVariable String driverId) {
        ApiResponse<Vehicle> response = vehicleService.getVehicleByDriverId(driverId);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update vehicle", description = "Update vehicle details")
    public ResponseEntity<ApiResponse<Vehicle>> updateVehicle(@PathVariable String id, @RequestBody VehicleRequest request) {
        ApiResponse<Vehicle> response = vehicleService.updateVehicle(id, request);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update vehicle status", description = "Update vehicle status")
    public ResponseEntity<ApiResponse<Vehicle>> updateVehicleStatus(@PathVariable String id, @RequestParam String status) {
        ApiResponse<Vehicle> response = vehicleService.updateVehicleStatus(id, status);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vehicle", description = "Delete a vehicle")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable String id) {
        ApiResponse<Void> response = vehicleService.deleteVehicle(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
}
