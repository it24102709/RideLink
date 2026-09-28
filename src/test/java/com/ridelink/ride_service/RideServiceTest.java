package com.ridelink.ride_service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ridelink.ride_service.model.Ride;
import com.ridelink.ride_service.repositary.RideRepository;
import com.ridelink.ride_service.service.RideService;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @InjectMocks
    private RideService rideService;

    @Test
    void requestRideSetsInitialStatusAndTimestamps() {
        Ride ride = new Ride();
        ride.setPassengerId("passenger-1");
        ride.setPickupLocation("Central Station");
        ride.setDropoffLocation("Airport");
        when(rideRepository.save(ride)).thenReturn(ride);

        Ride result = rideService.requestRide(ride);

        assertSame(ride, result);
        assertEquals("REQUESTED", result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertEquals(result.getCreatedAt(), result.getUpdatedAt());
        verify(rideRepository).save(ride);
    }

    @Test
    void assignDriverUpdatesDriverStatusAndTimestamp() {
        String rideId = "ride-1";
        String driverId = "driver-1";
        Ride ride = new Ride();
        ride.setId(rideId);
        ride.setStatus("REQUESTED");
        ride.setUpdatedAt(LocalDateTime.MIN);
        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(ride)).thenReturn(ride);

        Ride result = rideService.assignDriver(rideId, driverId);

        assertSame(ride, result);
        assertEquals(driverId, result.getDriverId());
        assertEquals("DRIVER_ASSIGNED", result.getStatus());
        assertNotNull(result.getUpdatedAt());
        verify(rideRepository).findById(rideId);
        verify(rideRepository).save(ride);
    }
}
