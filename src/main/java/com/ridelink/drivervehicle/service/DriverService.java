package com.ridelink.drivervehicle.service;

import com.ridelink.drivervehicle.dto.ApiResponse;
import com.ridelink.drivervehicle.dto.DriverRequest;
import com.ridelink.drivervehicle.model.Driver;
import com.ridelink.drivervehicle.model.Vehicle;
import com.ridelink.drivervehicle.repository.DriverRepository;
import com.ridelink.drivervehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {
    
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    
    public ApiResponse<Driver> createDriverProfile(DriverRequest request) {
        if (driverRepository.findByUserId(request.getUserId()).isPresent()) {
            return ApiResponse.error("Driver profile already exists for this user");
        }
        
        if (driverRepository.findByLicenseNumber(request.getLicenseNumber()).isPresent()) {
            return ApiResponse.error("License number already registered");
        }
        
        Driver driver = new Driver();
        driver.setUserId(request.getUserId());
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setLicenseExpiryDate(request.getLicenseExpiryDate());
        driver.setServiceArea(request.getServiceArea());
        driver.setAvailable(false);
        driver.setStatus("active");
        driver.setCreatedAt(LocalDateTime.now());
        driver.setUpdatedAt(LocalDateTime.now());
        
        if (request.getCurrentLocation() != null && request.getCurrentLocation().getCoordinates() != null) {
            GeoJsonPoint point = new GeoJsonPoint(
                request.getCurrentLocation().getCoordinates()[0],
                request.getCurrentLocation().getCoordinates()[1]
            );
            driver.setCurrentLocation(point);
            driver.setLocationName(request.getCurrentLocation().getLocationName());
        }
        
        Driver savedDriver = driverRepository.save(driver);
        return ApiResponse.success("Driver profile created successfully", savedDriver);
    }
    
    public ApiResponse<Driver> getDriverById(String id) {
        return driverRepository.findById(id)
            .map(driver -> ApiResponse.success("Driver profile retrieved", driver))
            .orElseGet(() -> ApiResponse.error("Driver profile not found"));
    }
    
    public ApiResponse<Driver> getDriverByUserId(String userId) {
        return driverRepository.findByUserId(userId)
            .map(driver -> ApiResponse.success("Driver profile retrieved", driver))
            .orElseGet(() -> ApiResponse.error("Driver profile not found"));
    }
    
    public ApiResponse<Driver> updateDriverProfile(String id, DriverRequest request) {
        return driverRepository.findById(id)
            .map(driver -> {
                if (request.getLicenseNumber() != null) {
                    driver.setLicenseNumber(request.getLicenseNumber());
                }
                if (request.getLicenseExpiryDate() != null) {
                    driver.setLicenseExpiryDate(request.getLicenseExpiryDate());
                }
                if (request.getServiceArea() != null) {
                    driver.setServiceArea(request.getServiceArea());
                }
                driver.setUpdatedAt(LocalDateTime.now());
                return ApiResponse.success("Driver profile updated successfully", driverRepository.save(driver));
            })
            .orElseGet(() -> ApiResponse.error("Driver profile not found"));
    }
    
    public ApiResponse<Driver> updateAvailability(String id, boolean isAvailable, DriverRequest.Location location) {
        return driverRepository.findById(id)
            .map(driver -> {
                if (isAvailable) {
                    Vehicle vehicle = vehicleRepository.findByDriverId(id).orElse(null);
                    if (vehicle == null || !"active".equals(vehicle.getStatus())) {
                        return ApiResponse.<Driver>error("Driver must have an active vehicle to be available");
                    }
                }
                
                driver.setAvailable(isAvailable);
                if (location != null && location.getCoordinates() != null) {
                    GeoJsonPoint point = new GeoJsonPoint(
                        location.getCoordinates()[0],
                        location.getCoordinates()[1]
                    );
                    driver.setCurrentLocation(point);
                    driver.setLocationName(location.getLocationName());
                }
                driver.setUpdatedAt(LocalDateTime.now());
                return ApiResponse.success("Availability status updated successfully", driverRepository.save(driver));
            })
            .orElseGet(() -> ApiResponse.<Driver>error("Driver profile not found"));
    }
    
    public ApiResponse<Driver> updateLocation(String id, DriverRequest.Location location) {
        return driverRepository.findById(id)
            .map(driver -> {
                if (location != null && location.getCoordinates() != null) {
                    GeoJsonPoint point = new GeoJsonPoint(
                        location.getCoordinates()[0],
                        location.getCoordinates()[1]
                    );
                    driver.setCurrentLocation(point);
                    driver.setLocationName(location.getLocationName());
                    driver.setUpdatedAt(LocalDateTime.now());
                }
                return ApiResponse.success("Location updated successfully", driverRepository.save(driver));
            })
            .orElseGet(() -> ApiResponse.error("Driver profile not found"));
    }
    
    public ApiResponse<List<Driver>> getAvailableDrivers(String serviceArea, double[] coordinates, double maxDistance) {
        List<Driver> drivers = driverRepository.findAvailableDriversNear(serviceArea, coordinates, maxDistance);
        return ApiResponse.success("Available drivers retrieved", drivers);
    }
    
    public ApiResponse<Driver> updateDriverRating(String id, double rating) {
        if (rating < 1 || rating > 5) {
            return ApiResponse.error("Rating must be between 1 and 5");
        }
        
        return driverRepository.findById(id)
            .map(driver -> {
                int totalRides = driver.getRating().getTotalRides() + 1;
                double currentTotal = driver.getRating().getAverage() * driver.getRating().getTotalRides();
                double newAverage = (currentTotal + rating) / totalRides;
                
                driver.getRating().setAverage(Math.round(newAverage * 100.0) / 100.0);
                driver.getRating().setTotalRides(totalRides);
                driver.setUpdatedAt(LocalDateTime.now());
                return ApiResponse.success("Rating updated successfully", driverRepository.save(driver));
            })
            .orElseGet(() -> ApiResponse.error("Driver profile not found"));
    }
}
