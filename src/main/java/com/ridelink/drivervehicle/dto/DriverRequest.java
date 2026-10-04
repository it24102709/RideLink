package com.ridelink.drivervehicle.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DriverRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "License number is required")
    @Size(min = 3, max = 20, message = "License number must be between 3 and 20 characters")
    private String licenseNumber;

    @NotNull(message = "License expiry date is required")
    @Future(message = "License expiry date must be in the future")
    private LocalDateTime licenseExpiryDate;

    @NotBlank(message = "Service area is required")
    private String serviceArea;

    private Location currentLocation;

    @Data
    public static class Location {
        @NotNull(message = "Coordinates are required")
        @Size(min = 2, max = 2, message = "Coordinates must contain exactly [longitude, latitude]")
        private double[] coordinates; // [longitude, latitude]
        private String locationName;
    }
}
