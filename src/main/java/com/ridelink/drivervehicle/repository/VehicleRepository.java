package com.ridelink.drivervehicle.repository;

import com.ridelink.drivervehicle.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    
    Optional<Vehicle> findByDriverId(String driverId);
    
    Optional<Vehicle> findByLicensePlate(String licensePlate);
}
