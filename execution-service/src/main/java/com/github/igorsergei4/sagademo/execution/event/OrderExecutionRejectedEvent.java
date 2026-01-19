package com.github.igorsergei4.sagademo.execution.event;

import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;
import com.github.igorsergei4.sagademo.execution.model.Offering;

import java.math.BigDecimal;

public record OrderExecutionRejectedEvent(
        Long orderId,
        RejectionReason rejectionReason,
        OfferingDto actualizedOffering
) {
    public enum RejectionReason {
        NO_SUCH_OFFERING,
        OFFERING_EXPIRED,
        NO_EXECUTORS
    }

    public static class OfferingDto extends NamedEntityDto {
        private BigDecimal cost;
        private boolean isDeprecated;

        public  OfferingDto(Long id) {
            setId(id);
        }

        public OfferingDto(Offering offering) {
            super(offering);
            this.cost = offering.getCost();
            this.isDeprecated = offering.isDeprecated();
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
