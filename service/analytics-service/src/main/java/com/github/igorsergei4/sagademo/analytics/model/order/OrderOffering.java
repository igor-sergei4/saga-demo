package com.github.igorsergei4.sagademo.analytics.model.order;

import com.github.igorsergei4.sagademo.analytics.model.NamedEntityProjection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "order_offering")
public class OrderOffering extends NamedEntityProjection {
    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "is_deprecated")
    private boolean isDeprecated;

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    public void setDeprecated(boolean deprecated) {
        isDeprecated = deprecated;
    }
}
