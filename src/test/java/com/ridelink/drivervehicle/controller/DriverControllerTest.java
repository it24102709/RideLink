package com.ridelink.drivervehicle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridelink.drivervehicle.dto.ApiResponse;
import com.ridelink.drivervehicle.dto.DriverRequest;
import com.ridelink.drivervehicle.model.Driver;
import com.ridelink.drivervehicle.service.DriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DriverController.class)
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    }

    @Test
    void createDriverProfile_Success() throws Exception {
        when(driverService.createDriverProfile(any(DriverRequest.class)))
                .thenReturn(ApiResponse.success("Driver profile created successfully", driver));

        mockMvc.perform(post("/api/drivers/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Driver profile created successfully"));
    }

    @Test
    void createDriverProfile_ValidationError() throws Exception {
        driverRequest.setUserId(""); // Invalid: empty user ID

        mockMvc.perform(post("/api/drivers/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getDriverById_Success() throws Exception {
        when(driverService.getDriverById(anyString()))
                .thenReturn(ApiResponse.success("Driver profile retrieved", driver));

        mockMvc.perform(get("/api/drivers/profile/driver123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Driver profile retrieved"));
    }

    @Test
    void getDriverById_NotFound() throws Exception {
        when(driverService.getDriverById(anyString()))
                .thenReturn(ApiResponse.error("Driver profile not found"));

        mockMvc.perform(get("/api/drivers/profile/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getDriverByUserId_Success() throws Exception {
        when(driverService.getDriverByUserId(anyString()))
                .thenReturn(ApiResponse.success("Driver profile retrieved", driver));

        mockMvc.perform(get("/api/drivers/user/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void updateDriverProfile_Success() throws Exception {
        when(driverService.updateDriverProfile(anyString(), any(DriverRequest.class)))
                .thenReturn(ApiResponse.success("Driver profile updated successfully", driver));

        mockMvc.perform(put("/api/drivers/profile/driver123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void updateAvailability_Success() throws Exception {
        when(driverService.updateAvailability(anyString(), anyBoolean(), any()))
                .thenReturn(ApiResponse.success("Availability status updated successfully", driver));

        mockMvc.perform(put("/api/drivers/availability/driver123?isAvailable=true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void updateDriverRating_Success() throws Exception {
        when(driverService.updateDriverRating(anyString(), anyDouble()))
                .thenReturn(ApiResponse.success("Rating updated successfully", driver));

        mockMvc.perform(put("/api/drivers/rating/driver123?rating=4.5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void updateDriverRating_InvalidRating() throws Exception {
        when(driverService.updateDriverRating(anyString(), anyDouble()))
                .thenReturn(ApiResponse.error("Rating must be between 1 and 5"));

        mockMvc.perform(put("/api/drivers/rating/driver123?rating=6.0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }
}
