package com.github.igorsergei4.sagademo.order.event;

import com.github.igorsergei4.sagademo.common.dto.DtoWithId;
import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;

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
) implements DtoWithId {
    public static final String TOPIC = "order-info";

    @Override
    public Long getId() {
        return id;
    }

    public static class OfferingDto extends NamedEntityDto {
        private BigDecimal cost;
        private boolean isDeprecated;

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
