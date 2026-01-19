package com.github.igorsergei4.sagademo.order.event;

import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;
import com.github.igorsergei4.sagademo.order.model.Offering;
import com.github.igorsergei4.sagademo.order.model.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OrderInfoEvent(
        Long id,
        NamedEntityDto client,
        OfferingDto offering,
        BigDecimal cost,
        NamedEntityDto executor,
        LocalDateTime createdAt,
        LocalDate deadlineOn,
        String status
) {
    public static final String TOPIC = "order-info";

    public OrderInfoEvent(Order order) {
        this(
                order.getId(),
                order.getClient() == null ? null : new NamedEntityDto(order.getClient()),
                order.getOffering() == null ? null : new OfferingDto(order.getOffering()),
                order.getCost(),
                order.getExecutor() == null ? null : new NamedEntityDto(order.getExecutor()),
                order.getCreatedAt(),
                order.getDeadlineOn(),
                order.getStatus().name()
        );
    }

    public static class OfferingDto extends NamedEntityDto {
        private BigDecimal cost;
        private boolean isDeprecated;

        public OfferingDto(Offering offering) {
            super(offering);
            this.cost = offering.getCost();
            this.isDeprecated = offering.getIsDeprecated();;
        }

        public OfferingDto() {
        }

        public BigDecimal getCost() {
            return cost;
        }

        public void setCost(BigDecimal cost) {
            this.cost = cost;
        }

        public boolean getIsDeprecated() {
            return isDeprecated;
        }

        public void setIsDeprecated(boolean isDeprecated) {
            this.isDeprecated = isDeprecated;
        }
    }
}
