package com.github.igorsergei4.sagademo.payment.event;

import com.github.igorsergei4.sagademo.common.dto.DtoWithId;
import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;

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
) implements DtoWithId {
    public static final String TOPIC = "order-payment-info";

    @Override
    public Long getId() {
        return id;
    }
}
