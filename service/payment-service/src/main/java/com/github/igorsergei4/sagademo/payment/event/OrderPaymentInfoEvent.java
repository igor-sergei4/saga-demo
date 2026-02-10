package com.github.igorsergei4.sagademo.payment.event;

import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;
import com.github.igorsergei4.sagademo.payment.model.OrderPayment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderPaymentInfoEvent(
        Long id,
        NamedEntityDto client,
        Long orderId,
        String paymentId,
        BigDecimal cost,
        LocalDateTime processedAt,
        String status
) {
    public static final String TOPIC = "order-payment-info";

    public OrderPaymentInfoEvent(OrderPayment orderPayment) {
        this(
                orderPayment.getId(),
                orderPayment.getClient() == null ? null : new NamedEntityDto(orderPayment.getClient()),
                orderPayment.getOrderId(),
                orderPayment.getPaymentId(),
                orderPayment.getCost(),
                orderPayment.getProcessedAt(),
                orderPayment.getStatus().name()
        );
    }
}
