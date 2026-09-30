package com.ridelink.drivervehicle.repository;

import com.ridelink.drivervehicle.model.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends MongoRepository<Driver, String> {
    
    Optional<Driver> findByUserId(String userId);
    
    Optional<Driver> findByLicenseNumber(String licenseNumber);
    
    List<Driver> findByIsAvailableTrueAndStatusAndServiceArea(String status, String serviceArea);
    
    @Query("{ 'isAvailable': true, 'status': 'active', 'serviceArea': ?0, 'currentLocation': { $near: { $geometry: { type: 'Point', coordinates: ?1 }, $maxDistance: ?2 } } }")
    List<Driver> findAvailableDriversNear(String serviceArea, double[] coordinates, double maxDistance);
}
