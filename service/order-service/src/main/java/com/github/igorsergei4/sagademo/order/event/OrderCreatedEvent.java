package com.github.igorsergei4.sagademo.order.event;

import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;
import com.github.igorsergei4.sagademo.order.model.Offering;
import com.github.igorsergei4.sagademo.order.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrderCreatedEvent(
        Long orderId,
        OfferingDto offering,
        NamedEntityDto client,
        LocalDateTime createdAt,
        LocalDate deadlineOn
) {
    public OrderCreatedEvent(Order order) {
        this(
                order.getId(),
                new OfferingDto(order.getOffering()),
                new NamedEntityDto(order.getClient()),
                order.getCreatedAt(),
                order.getDeadlineOn()
        );
    }

    public static class OfferingDto extends NamedEntityDto {
        private BigDecimal cost;

        public OfferingDto(Offering offering) {
            super(offering);
            this.cost = offering.getCost();
        }

        public OfferingDto() {}

        public BigDecimal getCost() {
            return cost;
        }

        public void setCost(BigDecimal cost) {
            this.cost = cost;
        }
    }
}
