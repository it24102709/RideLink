package com.ridelink.service;

import org.springframework.stereotype.Service;

import com.ridelink.model.Payment;
import com.ridelink.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment recordPayment(String rideId, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than 0"
            );
        }

        Payment payment = new Payment();

        payment.setRideId(rideId);
        payment.setAmount(amount);
        payment.setStatus("PAID");
        payment.setReceiptNumber("REC-" + System.currentTimeMillis());

        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(String id) {

        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found with id: " + id
                        )
                );
    }
}