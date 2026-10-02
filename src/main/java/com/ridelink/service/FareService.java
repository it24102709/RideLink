package com.ridelink.service;

import org.springframework.stereotype.Service;

import com.ridelink.model.Fare;
import com.ridelink.repository.FareRepository;

@Service
public class FareService {

    private final FareRepository fareRepository;

    private static final double BASE_FARE = 200.0;
    private static final double PER_KM_RATE = 100.0;

    public FareService(FareRepository fareRepository) {
        this.fareRepository = fareRepository;
    }

    public Fare calculateFare(String rideId, double distanceKm) {

        if (distanceKm <= 0) {
            throw new IllegalArgumentException(
                    "Distance must be greater than 0"
            );
        }

        double totalFare =
                BASE_FARE + (distanceKm * PER_KM_RATE);

        Fare fare = new Fare();

        fare.setRideId(rideId);
        fare.setDistanceKm(distanceKm);
        fare.setBaseFare(BASE_FARE);
        fare.setPerKmRate(PER_KM_RATE);
        fare.setTotalFare(totalFare);

        return fareRepository.save(fare);
    }

    public Fare getFareByRideId(String rideId) {

        return fareRepository.findByRideId(rideId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Fare not found for ride: " + rideId
                        )
                );
    }
}