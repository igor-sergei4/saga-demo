package com.github.igorsergei4.sagademo.execution.event;

import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;
import com.github.igorsergei4.sagademo.execution.model.Execution;
import com.github.igorsergei4.sagademo.execution.model.Executor;
import com.github.igorsergei4.sagademo.execution.model.Offering;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExecutionInfoEvent(
        Long id,
        NamedEntityDto client,
        Long orderId,
        OfferingDto offering,
        BigDecimal cost,
        ExecutorDto executor,
        LocalDateTime createdAt,
        LocalDate deadlineOn,
        String status
) {
    public static final String TOPIC = "execution-info";

    public ExecutionInfoEvent(Execution execution) {
        this(
                execution.getId(),
                execution.getClient() == null ? null : new NamedEntityDto(execution.getClient()),
                execution.getOrderId(),
                execution.getOffering() == null ? null : new OfferingDto(execution.getOffering()),
                execution.getCost(),
                execution.getExecutor() == null ? null : new ExecutorDto(execution.getExecutor()),
                execution.getCreatedAt(),
                execution.getDeadlineOn(),
                execution.getStatus().name()
        );
    }

    public static class OfferingDto extends NamedEntityDto {
        private BigDecimal cost;
        private Integer points;
        private LocalDateTime deprecatedAt;

        public OfferingDto(Offering offering) {
            super(offering);
            this.cost = offering.getCost();
            this.points = offering.getPoints();
            this.deprecatedAt = offering.getDeprecatedAt();
        }

        public OfferingDto() {
        }

        public BigDecimal getCost() {
            return cost;
        }

        public void setCost(BigDecimal cost) {
            this.cost = cost;
        }

        public Integer getPoints() {
            return points;
        }

        public void setPoints(Integer points) {
            this.points = points;
        }

        public LocalDateTime getDeprecatedAt() {
            return deprecatedAt;
        }

        public void setDeprecatedAt(LocalDateTime deprecatedAt) {
            this.deprecatedAt = deprecatedAt;
        }
    }

    public static class ExecutorDto extends NamedEntityDto {
        private Integer dailyPoints;
        private LocalDate finalDate;

        public ExecutorDto(Executor executor) {
            super(executor);
            this.dailyPoints = executor.getDailyPoints();
            this.finalDate = executor.getFinalDate();
        }

        public ExecutorDto() {
        }

        public Integer getDailyPoints() {
            return dailyPoints;
        }

        public void setDailyPoints(Integer dailyPoints) {
            this.dailyPoints = dailyPoints;
        }

        public LocalDate getFinalDate() {
            return finalDate;
        }

        public void setFinalDate(LocalDate finalDate) {
            this.finalDate = finalDate;
        }
    }
}
