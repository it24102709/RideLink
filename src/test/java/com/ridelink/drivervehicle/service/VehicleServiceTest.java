package com.ridelink.drivervehicle.service;

import com.ridelink.drivervehicle.dto.ApiResponse;
import com.ridelink.drivervehicle.dto.VehicleRequest;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private VehicleRequest vehicleRequest;
    private Vehicle vehicle;
    private Driver driver;

    @BeforeEach
    void setUp() {
        vehicleRequest = new VehicleRequest();
        vehicleRequest.setDriverId("driver123");
        vehicleRequest.setMake("Toyota");
        vehicleRequest.setModel("Corolla");
        vehicleRequest.setYear(2020);
        vehicleRequest.setLicensePlate("ABC-1234");
        vehicleRequest.setColor("White");
        vehicleRequest.setVehicleType("sedan");
        vehicleRequest.setCapacity(4);
        vehicleRequest.setRegistrationExpiryDate(LocalDateTime.now().plusYears(5));
        vehicleRequest.setInsuranceExpiryDate(LocalDateTime.now().plusYears(5));

        vehicle = new Vehicle();
        vehicle.setId("vehicle123");
        vehicle.setDriverId("driver123");
        vehicle.setMake("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setYear(2020);
        vehicle.setLicensePlate("ABC-1234");
        vehicle.setColor("White");
        vehicle.setVehicleType("sedan");
        vehicle.setCapacity(4);
        vehicle.setStatus("active");

        driver = new Driver();
        driver.setId("driver123");
        driver.setUserId("user123");
        driver.setLicenseNumber("DL12345");
        driver.setServiceArea("Colombo");
    }

    @Test
    void registerVehicle_Success() {
        when(driverRepository.findByUserId(anyString())).thenReturn(Optional.of(driver));
        when(vehicleRepository.findByDriverId(anyString())).thenReturn(Optional.empty());
        when(vehicleRepository.findByLicensePlate(anyString())).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        ApiResponse<Vehicle> response = vehicleService.registerVehicle(vehicleRequest);

        assertTrue(response.isSuccess());
        assertEquals("Vehicle registered successfully", response.getMessage());
        assertNotNull(response.getData());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void registerVehicle_DriverNotFound() {
        when(driverRepository.findByUserId(anyString())).thenReturn(Optional.empty());

        ApiResponse<Vehicle> response = vehicleService.registerVehicle(vehicleRequest);

        assertFalse(response.isSuccess());
        assertEquals("Driver profile not found", response.getMessage());
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void registerVehicle_VehicleAlreadyExists() {
        when(driverRepository.findByUserId(anyString())).thenReturn(Optional.of(driver));
        when(vehicleRepository.findByDriverId(anyString())).thenReturn(Optional.of(vehicle));

        ApiResponse<Vehicle> response = vehicleService.registerVehicle(vehicleRequest);

        assertFalse(response.isSuccess());
        assertEquals("Vehicle already registered for this driver", response.getMessage());
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void registerVehicle_LicensePlateAlreadyExists() {
        when(driverRepository.findByUserId(anyString())).thenReturn(Optional.of(driver));
        when(vehicleRepository.findByDriverId(anyString())).thenReturn(Optional.empty());
        when(vehicleRepository.findByLicensePlate(anyString())).thenReturn(Optional.of(vehicle));

        ApiResponse<Vehicle> response = vehicleService.registerVehicle(vehicleRequest);

        assertFalse(response.isSuccess());
        assertEquals("License plate already registered", response.getMessage());
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void getVehicleById_Success() {
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.of(vehicle));

        ApiResponse<Vehicle> response = vehicleService.getVehicleById("vehicle123");

        assertTrue(response.isSuccess());
        assertEquals("Vehicle retrieved", response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    void getVehicleById_NotFound() {
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.empty());

        ApiResponse<Vehicle> response = vehicleService.getVehicleById("nonexistent");

        assertFalse(response.isSuccess());
        assertEquals("Vehicle not found", response.getMessage());
    }

    @Test
    void getVehicleByDriverId_Success() {
        when(vehicleRepository.findByDriverId(anyString())).thenReturn(Optional.of(vehicle));

        ApiResponse<Vehicle> response = vehicleService.getVehicleByDriverId("driver123");

        assertTrue(response.isSuccess());
        assertEquals("Vehicle retrieved", response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    void updateVehicle_Success() {
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        ApiResponse<Vehicle> response = vehicleService.updateVehicle("vehicle123", vehicleRequest);

        assertTrue(response.isSuccess());
        assertEquals("Vehicle updated successfully", response.getMessage());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    void updateVehicleStatus_InactiveVehicle() {
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.of(vehicle));
        when(driverRepository.findById(anyString())).thenReturn(Optional.of(driver));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        ApiResponse<Vehicle> response = vehicleService.updateVehicleStatus("vehicle123", "inactive");

        assertTrue(response.isSuccess());
        assertEquals("Vehicle status updated successfully", response.getMessage());
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void deleteVehicle_Success() {
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.of(vehicle));
        when(driverRepository.findById(anyString())).thenReturn(Optional.of(driver));
        doNothing().when(vehicleRepository).deleteById(anyString());

        ApiResponse<Void> response = vehicleService.deleteVehicle("vehicle123");

        assertTrue(response.isSuccess());
        assertEquals("Vehicle deleted successfully", response.getMessage());
        verify(vehicleRepository, times(1)).deleteById(anyString());
        verify(driverRepository, times(1)).save(any(Driver.class));
    }
}
