package com.ridelink.drivervehicle.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VehicleRequest {

    @NotBlank(message = "Driver ID is required")
    private String driverId;

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @Min(value = 1980, message = "Year must be 1980 or later")
    private int year;

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotBlank(message = "Color is required")
    private String color;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 8, message = "Capacity cannot exceed 8")
    private int capacity;

    @Future(message = "Registration expiry date must be in the future")
    private LocalDateTime registrationExpiryDate;

    @Future(message = "Insurance expiry date must be in the future")
    private LocalDateTime insuranceExpiryDate;
}
