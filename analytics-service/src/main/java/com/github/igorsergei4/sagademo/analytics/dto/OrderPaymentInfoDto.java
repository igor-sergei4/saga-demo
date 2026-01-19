package com.github.igorsergei4.sagademo.analytics.dto;

import com.github.igorsergei4.sagademo.analytics.model.payment.OrderPayment;
import com.github.igorsergei4.sagademo.analytics.model.payment.PaymentClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderPaymentInfoDto(
        Long id,
        ClientDto client,
        String paymentId,
        BigDecimal cost,
        LocalDateTime processedAt,
        String status
) {
    public OrderPaymentInfoDto(OrderPayment orderPayment) {
        this(
                orderPayment.getId(),
                orderPayment.getClient() == null ? null : new ClientDto(orderPayment.getClient()),
                orderPayment.getPaymentId(),
                orderPayment.getCost(),
                orderPayment.getProcessedAt(),
                orderPayment.getStatus()
        );
    }

    public record ClientDto(Long id, String name) {
        public ClientDto(PaymentClient paymentClient) {
            this(paymentClient.getId(), paymentClient.getName());
        }
    }
}
