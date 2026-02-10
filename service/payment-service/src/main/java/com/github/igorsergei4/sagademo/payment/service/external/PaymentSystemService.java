package com.github.igorsergei4.sagademo.payment.service.external;

import com.github.igorsergei4.sagademo.payment.model.Client;

import java.math.BigDecimal;
import java.util.Optional;

public interface PaymentSystemService {
    Optional<String> commitPaymentAndGetPaymentId(Client _client, BigDecimal _cost);
}
