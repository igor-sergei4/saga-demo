package com.github.igorsergei4.sagademo.order.event;

import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;

import java.math.BigDecimal;

public record OrderRequiresPaymentEvent(Long orderId, BigDecimal cost, NamedEntityDto client) {
}
