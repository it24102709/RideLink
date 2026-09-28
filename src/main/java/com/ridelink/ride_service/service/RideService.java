package com.ridelink.ride_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ridelink.ride_service.model.Ride;
import com.ridelink.ride_service.repositary.RideRepository;

@Service
public class RideService {
    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public Ride requestRide(Ride ride) {
        LocalDateTime now = LocalDateTime.now();
        ride.setStatus("REQUESTED");
        ride.setCreatedAt(now);
        ride.setUpdatedAt(now);
        return rideRepository.save(ride);
    }

    public Ride assignDriver(String id, String driverId) {
        Ride ride = getRideById(id);
        ride.setDriverId(driverId);
        ride.setStatus("DRIVER_ASSIGNED");
        ride.setUpdatedAt(LocalDateTime.now());
        return rideRepository.save(ride);
    }

    public Ride updateLifecycleStatus(String id, String status) {
        Ride ride = getRideById(id);
        ride.setStatus(status);
        ride.setUpdatedAt(LocalDateTime.now());
        return rideRepository.save(ride);
    }

    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    public Ride getRideById(String id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ride not found: " + id));
    }
}
