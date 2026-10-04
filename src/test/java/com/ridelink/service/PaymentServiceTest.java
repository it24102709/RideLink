package com.ridelink.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ridelink.model.Payment;
import com.ridelink.repository.PaymentRepository;

class PaymentServiceTest {

    @Test
    void recordPayment_shouldRecordPayment() {

        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        PaymentService paymentService = new PaymentService(paymentRepository);

        Payment savedPayment = new Payment();
        savedPayment.setRideId("R100");
        savedPayment.setAmount(1200);
        savedPayment.setStatus("PAID");
        savedPayment.setReceiptNumber("REC-100");

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(savedPayment);

        Payment result = paymentService.recordPayment("R100", 1200);

        assertEquals("R100", result.getRideId());
        assertEquals(1200, result.getAmount());
        assertEquals("PAID", result.getStatus());
        assertEquals("REC-100", result.getReceiptNumber());

        verify(paymentRepository, times(1))
                .save(any(Payment.class));
    }

    @Test
    void recordPayment_shouldRejectInvalidAmount() {

        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        PaymentService paymentService = new PaymentService(paymentRepository);

        assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.recordPayment("R101", 0)
        );

        verify(paymentRepository, never())
                .save(any(Payment.class));
    }

    @Test
    void getPaymentById_shouldReturnPayment() {

        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        PaymentService paymentService = new PaymentService(paymentRepository);

        Payment payment = new Payment();
        payment.setRideId("R102");
        payment.setAmount(1500);
        payment.setStatus("PAID");
        payment.setReceiptNumber("REC-102");

        when(paymentRepository.findById("P102"))
                .thenReturn(Optional.of(payment));

        Payment result = paymentService.getPaymentById("P102");

        assertEquals("R102", result.getRideId());
        assertEquals(1500, result.getAmount());
        assertEquals("PAID", result.getStatus());
        assertEquals("REC-102", result.getReceiptNumber());
    }

    @Test
    void getPaymentById_shouldThrowExceptionWhenNotFound() {

        PaymentRepository paymentRepository = mock(PaymentRepository.class);
        PaymentService paymentService = new PaymentService(paymentRepository);

        when(paymentRepository.findById("P999"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> paymentService.getPaymentById("P999")
        );
    }
}