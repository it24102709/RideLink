package com.ridelink.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ridelink.model.Payment;
import com.ridelink.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Payment recordPayment(
            @RequestParam String rideId,
            @RequestParam double amount) {

        return paymentService.recordPayment(rideId, amount);
    }

    @GetMapping("/{id}")
    public Payment getPayment(@PathVariable String id) {

        return paymentService.getPaymentById(id);
    }
}