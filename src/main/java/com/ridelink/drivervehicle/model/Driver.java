package com.ridelink.drivervehicle.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;

@Data
@Document(collection = "drivers")
public class Driver {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String userId;
    
    @Indexed(unique = true)
    private String licenseNumber;
    
    private LocalDateTime licenseExpiryDate;
    
    private boolean isAvailable = false;
    
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint currentLocation;
    
    private String locationName;
    
    private String serviceArea;
    
    private Rating rating = new Rating();
    
    private String status = "active";
    
    @DBRef
    private Vehicle vehicle;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Data
    public static class Rating {
        private double average = 0.0;
        private int totalRides = 0;
    }
}
