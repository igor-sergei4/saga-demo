package com.github.igorsergei4.sagademo.order.event;

import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;

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
    public static class OfferingDto extends NamedEntityDto {
        private BigDecimal cost;

        public BigDecimal getCost() {
            return cost;
        }

        public void setCost(BigDecimal cost) {
            this.cost = cost;
        }
    }
}
