package com.ridelink.drivervehicle.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "vehicles")
public class Vehicle {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String driverId;
    
    private String make;
    private String model;
    private int year;
    
    @Indexed(unique = true)
    private String licensePlate;
    
    private String color;
    private String vehicleType;
    private int capacity;
    
    private LocalDateTime registrationExpiryDate;
    private LocalDateTime insuranceExpiryDate;
    
    private String status = "active";
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
