package com.ridelink.ride_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ridelink.ride_service.model.Ride;
import com.ridelink.ride_service.service.RideService;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    @Autowired
    private RideService rideService;

    // 1. Ride Request API
    @PostMapping("/request")
    public Ride requestRide(@RequestBody Ride ride) {
        return rideService.requestRide(ride);
    }

    // 2. Driver Assignment API
    @PutMapping("/{id}/assign-driver")
    public Ride assignDriver(@PathVariable String id, @RequestParam String driverId) {
        return rideService.assignDriver(id, driverId);
    }

    // 3. Ride Lifecycle API
    @PutMapping("/{id}/status")
    public Ride updateStatus(@PathVariable String id, @RequestParam String status) {
        return rideService.updateLifecycleStatus(id, status);
    }

    @GetMapping
    public List<Ride> getAllRides() {
        return rideService.getAllRides();
    }

    @GetMapping("/{id}")
    public Ride getRideById(@PathVariable String id) {
        return rideService.getRideById(id);
    }
}