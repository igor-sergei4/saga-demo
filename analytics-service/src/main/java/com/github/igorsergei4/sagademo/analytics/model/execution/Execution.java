package com.github.igorsergei4.sagademo.analytics.model.execution;

import com.github.igorsergei4.sagademo.analytics.model.EntityWithIdProjection;
import com.github.igorsergei4.sagademo.analytics.model.order.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "execution")
public class Execution extends EntityWithIdProjection {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_execution_client")
    private ExecutionClient client;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_execution_offering")
    private ExecutionOffering offering;

    @Column(name = "cost")
    private BigDecimal cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_execution_executor")
    private ExecutionExecutor executor;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deadline_on")
    private LocalDate deadlineOn;

    @Column(name = "status")
    private String status;

    public ExecutionClient getClient() {
        return client;
    }

    public void setClient(ExecutionClient client) {
        this.client = client;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ExecutionOffering getOffering() {
        return offering;
    }

    public void setOffering(ExecutionOffering offering) {
        this.offering = offering;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public ExecutionExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutionExecutor executor) {
        this.executor = executor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getDeadlineOn() {
        return deadlineOn;
    }

    public void setDeadlineOn(LocalDate deadlineOn) {
        this.deadlineOn = deadlineOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
