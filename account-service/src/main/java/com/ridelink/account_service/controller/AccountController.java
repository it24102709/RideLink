package com.ridelink.account_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.ridelink.account_service.model.Account;
import com.ridelink.account_service.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // Create account
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    // Get all accounts
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    // Get account by ID
    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable String id) {
        return accountService.getAccountById(id);
    }

    // Delete account
    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable String id) {
        accountService.deleteAccount(id);
        return "Account deleted successfully";
    }

    // Login
    @PostMapping("/login")
    public boolean login(@RequestBody Account account) {
        return accountService.login(
                account.getEmail(),
                account.getPassword()
        );
    }
}