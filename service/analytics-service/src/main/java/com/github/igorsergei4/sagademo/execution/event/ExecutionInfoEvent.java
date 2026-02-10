package com.github.igorsergei4.sagademo.execution.event;

import com.github.igorsergei4.sagademo.common.dto.DtoWithId;
import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;

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
) implements DtoWithId {
    public static final String TOPIC = "execution-info";

    @Override
    public Long getId() {
        return id;
    }

    public static class OfferingDto extends NamedEntityDto {
        private BigDecimal cost;
        private Integer points;
        private LocalDateTime deprecatedAt;

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
