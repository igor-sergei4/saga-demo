package com.github.igorsergei4.sagademo.payment.service.external;

import com.github.igorsergei4.sagademo.payment.model.Client;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Service
public class PaymentSystemServiceStub implements PaymentSystemService {
    private static final double FAILURE_RATE = 1.0 / 5;

    @Override
    public Optional<String> commitPaymentAndGetPaymentId(Client _client, BigDecimal _cost) {
        if (Math.random() < FAILURE_RATE) {
            return Optional.empty();
        } else {
            // Relatively "unique" identifier
            return Optional.of(String.valueOf(Instant.now().toEpochMilli()));
        }
    }
}
