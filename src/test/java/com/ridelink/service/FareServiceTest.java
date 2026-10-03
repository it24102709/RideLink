package com.ridelink.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ridelink.model.Fare;
import com.ridelink.repository.FareRepository;

class FareServiceTest {

    @Test
    void calculateFare_shouldCalculateAndSaveFare() {

        FareRepository fareRepository = mock(FareRepository.class);
        FareService fareService = new FareService(fareRepository);

        Fare savedFare = new Fare();
        savedFare.setRideId("R100");
        savedFare.setDistanceKm(10);
        savedFare.setBaseFare(200);
        savedFare.setPerKmRate(100);
        savedFare.setTotalFare(1200);

        when(fareRepository.save(any(Fare.class)))
                .thenReturn(savedFare);

        Fare result = fareService.calculateFare("R100", 10);

        assertEquals("R100", result.getRideId());
        assertEquals(10, result.getDistanceKm());
        assertEquals(200, result.getBaseFare());
        assertEquals(100, result.getPerKmRate());
        assertEquals(1200, result.getTotalFare());

        verify(fareRepository, times(1))
                .save(any(Fare.class));
    }

    @Test
    void calculateFare_shouldRejectInvalidDistance() {

        FareRepository fareRepository = mock(FareRepository.class);
        FareService fareService = new FareService(fareRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> fareService.calculateFare("R101", 0)
        );

        verify(fareRepository, never())
                .save(any(Fare.class));
    }

    @Test
    void getFareByRideId_shouldReturnFare() {

        FareRepository fareRepository = mock(FareRepository.class);
        FareService fareService = new FareService(fareRepository);

        Fare fare = new Fare();
        fare.setRideId("R102");
        fare.setDistanceKm(5);
        fare.setTotalFare(700);

        when(fareRepository.findByRideId("R102"))
                .thenReturn(Optional.of(fare));

        Fare result = fareService.getFareByRideId("R102");

        assertEquals("R102", result.getRideId());
        assertEquals(700, result.getTotalFare());
    }

    @Test
    void getFareByRideId_shouldThrowExceptionWhenNotFound() {

        FareRepository fareRepository = mock(FareRepository.class);
        FareService fareService = new FareService(fareRepository);

        when(fareRepository.findByRideId("R999"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> fareService.getFareByRideId("R999")
        );
    }
}