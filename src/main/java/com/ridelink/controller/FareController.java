package com.ridelink.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ridelink.model.Fare;
import com.ridelink.service.FareService;

@RestController
@RequestMapping("/api/fares")
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    @PostMapping
    public Fare calculateFare(
            @RequestParam String rideId,
            @RequestParam double distanceKm) {

        return fareService.calculateFare(rideId, distanceKm);
    }

    @GetMapping("/{rideId}")
    public Fare getFare(@PathVariable String rideId) {

        return fareService.getFareByRideId(rideId);
    }
}