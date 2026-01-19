package com.github.igorsergei4.sagademo.analytics.model.execution;

import com.github.igorsergei4.sagademo.analytics.model.NamedEntityProjection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "execution_offering")
public class ExecutionOffering extends NamedEntityProjection {
    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "points")
    private Integer points;

    @Column(name = "deprecated_at")
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
