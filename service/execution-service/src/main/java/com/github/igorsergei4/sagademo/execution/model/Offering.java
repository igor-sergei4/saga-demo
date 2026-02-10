package com.github.igorsergei4.sagademo.execution.model;

import com.github.igorsergei4.sagademo.common.model.NamedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "offering")
public class Offering extends NamedEntity {
    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "deprecated_at", nullable = true)
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

    public boolean isDeprecated() {
        return deprecatedAt != null && !deprecatedAt.isAfter(LocalDateTime.now());
    }
}
