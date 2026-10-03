package com.ridelink.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @PostMapping(consumes = "application/json")
    public Payment recordPayment(@RequestBody PaymentRequest request) {

        return paymentService.recordPayment(
                request.rideId,
                request.amount
        );
    }

    @GetMapping("/{id}")
    public Payment getPayment(@PathVariable String id) {

        return paymentService.getPaymentById(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(
            IllegalArgumentException ex) {

        return ex.getMessage();
    }

    public static class PaymentRequest {

        public String rideId;
        public double amount;

        public PaymentRequest() {
        }
    }
}