package com.ridelink.drivervehicle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridelink.drivervehicle.dto.ApiResponse;
import com.ridelink.drivervehicle.dto.VehicleRequest;
import com.ridelink.drivervehicle.model.Vehicle;
import com.ridelink.drivervehicle.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VehicleService vehicleService;

    private VehicleRequest vehicleRequest;
    private Vehicle vehicle;

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
    }

    @Test
    void registerVehicle_Success() throws Exception {
        when(vehicleService.registerVehicle(any(VehicleRequest.class)))
                .thenReturn(ApiResponse.success("Vehicle registered successfully", vehicle));

        mockMvc.perform(post("/api/vehicles/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Vehicle registered successfully"));
    }

    @Test
    void registerVehicle_ValidationError() throws Exception {
        vehicleRequest.setDriverId(""); // Invalid: empty driver ID

        mockMvc.perform(post("/api/vehicles/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getVehicleById_Success() throws Exception {
        when(vehicleService.getVehicleById(anyString()))
                .thenReturn(ApiResponse.success("Vehicle retrieved", vehicle));

        mockMvc.perform(get("/api/vehicles/vehicle123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Vehicle retrieved"));
    }

    @Test
    void getVehicleById_NotFound() throws Exception {
        when(vehicleService.getVehicleById(anyString()))
                .thenReturn(ApiResponse.error("Vehicle not found"));

        mockMvc.perform(get("/api/vehicles/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getVehicleByDriverId_Success() throws Exception {
        when(vehicleService.getVehicleByDriverId(anyString()))
                .thenReturn(ApiResponse.success("Vehicle retrieved", vehicle));

        mockMvc.perform(get("/api/vehicles/driver/driver123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void updateVehicle_Success() throws Exception {
        when(vehicleService.updateVehicle(anyString(), any(VehicleRequest.class)))
                .thenReturn(ApiResponse.success("Vehicle updated successfully", vehicle));

        mockMvc.perform(put("/api/vehicles/vehicle123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void updateVehicleStatus_Success() throws Exception {
        when(vehicleService.updateVehicleStatus(anyString(), anyString()))
                .thenReturn(ApiResponse.success("Vehicle status updated successfully", vehicle));

        mockMvc.perform(put("/api/vehicles/vehicle123/status?status=inactive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void deleteVehicle_Success() throws Exception {
        when(vehicleService.deleteVehicle(anyString()))
                .thenReturn(ApiResponse.success("Vehicle deleted successfully", null));

        mockMvc.perform(delete("/api/vehicles/vehicle123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Vehicle deleted successfully"));
    }

    @Test
    void deleteVehicle_NotFound() throws Exception {
        when(vehicleService.deleteVehicle(anyString()))
                .thenReturn(ApiResponse.error("Vehicle not found"));

        mockMvc.perform(delete("/api/vehicles/nonexistent"))
                .andExpect(status().isNotFound());
    }
}
