package com.ridelink.drivervehicle.service;

import com.ridelink.drivervehicle.dto.ApiResponse;
import com.ridelink.drivervehicle.dto.DriverRequest;
import com.ridelink.drivervehicle.model.Driver;
import com.ridelink.drivervehicle.model.Vehicle;
import com.ridelink.drivervehicle.repository.DriverRepository;
import com.ridelink.drivervehicle.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private DriverService driverService;

    private DriverRequest driverRequest;
    private Driver driver;

    @BeforeEach
    void setUp() {
        driverRequest = new DriverRequest();
        driverRequest.setUserId("507f1f77bcf86cd799439011");
        driverRequest.setLicenseNumber("DL12345");
        driverRequest.setLicenseExpiryDate(LocalDateTime.now().plusYears(5));
        driverRequest.setServiceArea("Colombo");

        driver = new Driver();
        driver.setId("driver123");
        driver.setUserId("507f1f77bcf86cd799439011");
        driver.setLicenseNumber("DL12345");
        driver.setLicenseExpiryDate(LocalDateTime.now().plusYears(5));
        driver.setServiceArea("Colombo");
        driver.setAvailable(false);
        driver.setStatus("active");
    }

    @Test
    void createDriverProfile_Success() {
        when(driverRepository.findByUserId(anyString())).thenReturn(Optional.empty());
        when(driverRepository.findByLicenseNumber(anyString())).thenReturn(Optional.empty());
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        ApiResponse<Driver> response = driverService.createDriverProfile(driverRequest);

        assertTrue(response.isSuccess());
        assertEquals("Driver profile created successfully", response.getMessage());
        assertNotNull(response.getData());
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void createDriverProfile_UserAlreadyExists() {
        when(driverRepository.findByUserId(anyString())).thenReturn(Optional.of(driver));

        ApiResponse<Driver> response = driverService.createDriverProfile(driverRequest);

        assertFalse(response.isSuccess());
        assertEquals("Driver profile already exists for this user", response.getMessage());
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void createDriverProfile_LicenseAlreadyExists() {
        when(driverRepository.findByUserId(anyString())).thenReturn(Optional.empty());
        when(driverRepository.findByLicenseNumber(anyString())).thenReturn(Optional.of(driver));

        ApiResponse<Driver> response = driverService.createDriverProfile(driverRequest);

        assertFalse(response.isSuccess());
        assertEquals("License number already registered", response.getMessage());
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void getDriverById_Success() {
        when(driverRepository.findById(anyString())).thenReturn(Optional.of(driver));

        ApiResponse<Driver> response = driverService.getDriverById("driver123");

        assertTrue(response.isSuccess());
        assertEquals("Driver profile retrieved", response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    void getDriverById_NotFound() {
        when(driverRepository.findById(anyString())).thenReturn(Optional.empty());

        ApiResponse<Driver> response = driverService.getDriverById("nonexistent");

        assertFalse(response.isSuccess());
        assertEquals("Driver profile not found", response.getMessage());
    }

    @Test
    void updateDriverProfile_Success() {
        when(driverRepository.findById(anyString())).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        ApiResponse<Driver> response = driverService.updateDriverProfile("driver123", driverRequest);

        assertTrue(response.isSuccess());
        assertEquals("Driver profile updated successfully", response.getMessage());
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void updateAvailability_WithActiveVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setStatus("active");
        
        when(driverRepository.findById(anyString())).thenReturn(Optional.of(driver));
        when(vehicleRepository.findByDriverId(anyString())).thenReturn(Optional.of(vehicle));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        ApiResponse<Driver> response = driverService.updateAvailability("driver123", true, null);

        assertTrue(response.isSuccess());
        assertEquals("Availability status updated successfully", response.getMessage());
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void updateAvailability_WithoutActiveVehicle() {
        when(driverRepository.findById(anyString())).thenReturn(Optional.of(driver));
        when(vehicleRepository.findByDriverId(anyString())).thenReturn(Optional.empty());

        ApiResponse<Driver> response = driverService.updateAvailability("driver123", true, null);

        assertFalse(response.isSuccess());
        assertEquals("Driver must have an active vehicle to be available", response.getMessage());
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    void updateDriverRating_Success() {
        when(driverRepository.findById(anyString())).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        ApiResponse<Driver> response = driverService.updateDriverRating("driver123", 4.5);

        assertTrue(response.isSuccess());
        assertEquals("Rating updated successfully", response.getMessage());
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void updateDriverRating_InvalidRating() {
        ApiResponse<Driver> response = driverService.updateDriverRating("driver123", 6.0);

        assertFalse(response.isSuccess());
        assertEquals("Rating must be between 1 and 5", response.getMessage());
        verify(driverRepository, never()).save(any(Driver.class));
    }
}
