package com.ridelink.drivervehicle.service;

import com.ridelink.drivervehicle.dto.ApiResponse;
import com.ridelink.drivervehicle.dto.VehicleRequest;
import com.ridelink.drivervehicle.model.Driver;
import com.ridelink.drivervehicle.model.Vehicle;
import com.ridelink.drivervehicle.repository.DriverRepository;
import com.ridelink.drivervehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VehicleService {
    
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    
    public ApiResponse<Vehicle> registerVehicle(VehicleRequest request) {
        Driver driver = driverRepository.findById(request.getDriverId()).orElse(null);
        if (driver == null) {
            return ApiResponse.error("Driver profile not found");
        }
        
        if (vehicleRepository.findByDriverId(request.getDriverId()).isPresent()) {
            return ApiResponse.error("Vehicle already registered for this driver");
        }
        
        if (vehicleRepository.findByLicensePlate(request.getLicensePlate()).isPresent()) {
            return ApiResponse.error("License plate already registered");
        }
        
        Vehicle vehicle = new Vehicle();
        vehicle.setDriverId(request.getDriverId());
        vehicle.setMake(request.getMake());
        vehicle.setModel(request.getModel());
        vehicle.setYear(request.getYear());
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setColor(request.getColor());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setCapacity(request.getCapacity());
        vehicle.setRegistrationExpiryDate(request.getRegistrationExpiryDate());
        vehicle.setInsuranceExpiryDate(request.getInsuranceExpiryDate());
        vehicle.setStatus("active");
        vehicle.setCreatedAt(LocalDateTime.now());
        vehicle.setUpdatedAt(LocalDateTime.now());
        
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        
        driver.setVehicle(savedVehicle);
        driverRepository.save(driver);
        
        return ApiResponse.success("Vehicle registered successfully", savedVehicle);
    }
    
    public ApiResponse<Vehicle> getVehicleById(String id) {
        return vehicleRepository.findById(id)
            .map(vehicle -> ApiResponse.success("Vehicle retrieved", vehicle))
            .orElseGet(() -> ApiResponse.error("Vehicle not found"));
    }
    
    public ApiResponse<Vehicle> getVehicleByDriverId(String driverId) {
        return vehicleRepository.findByDriverId(driverId)
            .map(vehicle -> ApiResponse.success("Vehicle retrieved", vehicle))
            .orElseGet(() -> ApiResponse.error("Vehicle not found for this driver"));
    }
    
    public ApiResponse<Vehicle> updateVehicle(String id, VehicleRequest request) {
        return vehicleRepository.findById(id)
            .map(vehicle -> {
                if (request.getMake() != null) vehicle.setMake(request.getMake());
                if (request.getModel() != null) vehicle.setModel(request.getModel());
                if (request.getYear() > 0) vehicle.setYear(request.getYear());
                if (request.getColor() != null) vehicle.setColor(request.getColor());
                if (request.getVehicleType() != null) vehicle.setVehicleType(request.getVehicleType());
                if (request.getCapacity() > 0) vehicle.setCapacity(request.getCapacity());
                if (request.getRegistrationExpiryDate() != null) vehicle.setRegistrationExpiryDate(request.getRegistrationExpiryDate());
                if (request.getInsuranceExpiryDate() != null) vehicle.setInsuranceExpiryDate(request.getInsuranceExpiryDate());
                vehicle.setUpdatedAt(LocalDateTime.now());
                return ApiResponse.success("Vehicle updated successfully", vehicleRepository.save(vehicle));
            })
            .orElseGet(() -> ApiResponse.error("Vehicle not found"));
    }
    
    public ApiResponse<Vehicle> updateVehicleStatus(String id, String status) {
        return vehicleRepository.findById(id)
            .map(vehicle -> {
                if (!"active".equals(status)) {
                    Driver driver = driverRepository.findById(vehicle.getDriverId()).orElse(null);
                    if (driver != null) {
                        driver.setAvailable(false);
                        driverRepository.save(driver);
                    }
                }
                vehicle.setStatus(status);
                vehicle.setUpdatedAt(LocalDateTime.now());
                return ApiResponse.success("Vehicle status updated successfully", vehicleRepository.save(vehicle));
            })
            .orElseGet(() -> ApiResponse.error("Vehicle not found"));
    }
    
    public ApiResponse<Void> deleteVehicle(String id) {
        return vehicleRepository.findById(id)
            .map(vehicle -> {
                Driver driver = driverRepository.findById(vehicle.getDriverId()).orElse(null);
                if (driver != null) {
                    driver.setVehicle(null);
                    driver.setAvailable(false);
                    driverRepository.save(driver);
                }
                vehicleRepository.deleteById(id);
                return ApiResponse.<Void>success("Vehicle deleted successfully", null);
            })
            .orElseGet(() -> ApiResponse.<Void>error("Vehicle not found"));
    }
}
