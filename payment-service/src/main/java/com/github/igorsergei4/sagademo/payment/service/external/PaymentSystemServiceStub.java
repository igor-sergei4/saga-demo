package com.github.igorsergei4.sagademo.payment.service.external;

import com.github.igorsergei4.sagademo.payment.model.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
public class PaymentSystemServiceStub implements PaymentSystemService {
    @Value("${payment-system.failure-rate}")
    private double failureRate;

    @Override
    public Optional<String> commitPaymentAndGetPaymentId(Client _client, BigDecimal _cost) {
        if (Math.random() < failureRate) {
            return Optional.empty();
        } else {
            // Relatively "unique" identifier
            return Optional.of(String.valueOf(Instant.now().toEpochMilli()));
        }
    }
}
