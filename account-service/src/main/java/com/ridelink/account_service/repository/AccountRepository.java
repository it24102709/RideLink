package com.ridelink.account_service.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ridelink.account_service.model.Account;

public interface AccountRepository extends MongoRepository<Account, String> {

    Optional<Account> findByEmail(String email);

}