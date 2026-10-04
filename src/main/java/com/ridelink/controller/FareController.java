package com.ridelink.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @PostMapping(consumes = "application/json")
    public Fare calculateFare(@RequestBody FareRequest request) {

        return fareService.calculateFare(
                request.rideId,
                request.distanceKm
        );
    }

    @GetMapping("/{rideId}")
    public Fare getFare(@PathVariable String rideId) {

        return fareService.getFareByRideId(rideId);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(
            IllegalArgumentException ex) {

        return ex.getMessage();
    }

    public static class FareRequest {

        public String rideId;
        public double distanceKm;

        public FareRequest() {
        }
    }
}