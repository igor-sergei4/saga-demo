package com.github.igorsergei4.sagademo.order.model;

import com.github.igorsergei4.sagademo.common.model.NamedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "offering")
public class Offering extends NamedEntity {
    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "is_deprecated")
    private boolean isDeprecated;

    @Column(name = "remote_id", nullable = false, unique = true)
    private Long remoteId;

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


    public Long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }
}
