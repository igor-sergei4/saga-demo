package com.github.igorsergei4.sagademo.analytics.model.order;

import com.github.igorsergei4.sagademo.analytics.model.EntityWithIdProjection;
import com.github.igorsergei4.sagademo.analytics.model.execution.Execution;
import com.github.igorsergei4.sagademo.analytics.model.payment.OrderPayment;
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
@Table(name = "\"order\"")
public class Order extends EntityWithIdProjection {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_client")
    private OrderClient client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_offering")
    private OrderOffering offering;

    @Column(name = "cost")
    private BigDecimal cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order_executor")
    private OrderExecutor executor;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deadline_on")
    private LocalDate deadlineOn;

    @Column(name = "status")
    private String status;

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, optional = true)
    private Execution execution;

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY, optional = true)
    private OrderPayment orderPayment;

    public OrderClient getClient() {
        return client;
    }

    public void setClient(OrderClient client) {
        this.client = client;
    }

    public OrderOffering getOffering() {
        return offering;
    }

    public void setOffering(OrderOffering offering) {
        this.offering = offering;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public OrderExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(OrderExecutor executor) {
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

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public OrderPayment getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(OrderPayment orderPayment) {
        this.orderPayment = orderPayment;
    }
}
