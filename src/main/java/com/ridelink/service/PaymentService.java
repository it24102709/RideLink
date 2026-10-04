package com.ridelink.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.ridelink.model.Payment;
import com.ridelink.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RestClient restClient;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;

        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
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

        Payment savedPayment = paymentRepository.save(payment);

        // Notify Ride Management Service after successful payment
        try {
            restClient.put()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/rides/{id}/status")
                            .queryParam("status", "COMPLETED")
                            .build(rideId))
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception ex) {
            System.out.println(
                    "Warning: Could not update ride status: "
                            + ex.getMessage()
            );
        }

        return savedPayment;
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