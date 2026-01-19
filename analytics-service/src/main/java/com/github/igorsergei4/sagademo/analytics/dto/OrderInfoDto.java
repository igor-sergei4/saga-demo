package com.github.igorsergei4.sagademo.analytics.dto;

import com.github.igorsergei4.sagademo.analytics.model.NamedEntityProjection;
import com.github.igorsergei4.sagademo.analytics.model.order.Order;
import com.github.igorsergei4.sagademo.analytics.model.order.OrderOffering;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrderInfoDto(
        Long id,
        NamedEntityDto client,
        OfferingDto offering,
        BigDecimal cost,
        NamedEntityDto executor,
        LocalDateTime createdAt,
        LocalDate deadlineOn,
        String status,
        ExecutionInfoDto execution,
        OrderPaymentInfoDto payment
) {
    public OrderInfoDto(Order order) {
        this(
                order.getId(),
                order.getClient() == null ? null : new NamedEntityDto(order.getClient()),
                order.getOffering() == null ? null : new OfferingDto(order.getOffering()),
                order.getCost(),
                order.getExecutor() == null ? null : new NamedEntityDto(order.getExecutor()),
                order.getCreatedAt(),
                order.getDeadlineOn(),
                order.getStatus(),
                order.getExecution() == null ? null : new ExecutionInfoDto(order.getExecution()),
                order.getOrderPayment() == null ? null : new OrderPaymentInfoDto(order.getOrderPayment())
        );
    }

    public record NamedEntityDto(Long id, String name) {
        public NamedEntityDto(NamedEntityProjection namedEntityProjection) {
            this(namedEntityProjection.getId(), namedEntityProjection.getName());
        }
    }

    public record OfferingDto(
            Long id,
            String name,
            BigDecimal cost,
            boolean isDeprecated
    ) {
        public OfferingDto(OrderOffering orderOffering) {
            this(
                    orderOffering.getId(),
                    orderOffering.getName(),
                    orderOffering.getCost(),
                    orderOffering.isDeprecated()
            );

        }
    }
}
