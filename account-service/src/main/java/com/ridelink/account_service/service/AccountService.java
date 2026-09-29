package com.ridelink.account_service.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ridelink.account_service.model.Account;
import com.ridelink.account_service.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    // Create account
    public Account createAccount(Account account) {

        // Check whether the email is already registered
        if (accountRepository.findByEmail(account.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Hash the password before saving
        String hashedPassword =
                passwordEncoder.encode(account.getPassword());

        account.setPassword(hashedPassword);

        return accountRepository.save(account);
    }

    // Get all accounts
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Get account by ID
    public Account getAccountById(String id) {
        return accountRepository.findById(id).orElse(null);
    }

    // Delete account
    public void deleteAccount(String id) {
        accountRepository.deleteById(id);
    }

    // Login
    public boolean login(String email, String password) {

        return accountRepository.findByEmail(email)
                .map(account ->
                        passwordEncoder.matches(
                                password,
                                account.getPassword()
                        )
                )
                .orElse(false);
    }
}