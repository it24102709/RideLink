package com.ridelink.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ridelink.model.Payment;

public interface PaymentRepository extends MongoRepository<Payment, String> {
}
