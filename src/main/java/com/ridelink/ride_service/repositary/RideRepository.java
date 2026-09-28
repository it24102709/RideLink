package com.ridelink.ride_service.repositary;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ridelink.ride_service.model.Ride;

@Repository
public interface RideRepository extends MongoRepository<Ride, String> {
}
