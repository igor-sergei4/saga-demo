package com.github.igorsergei4.sagademo.execution.model;

import com.github.igorsergei4.sagademo.common.model.EntityWithId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "execution")
public class Execution extends EntityWithId {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @Column(name = "order_id", nullable = true, unique = true)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_offering", nullable = false)
    private Offering offering;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_executor", nullable = true)
    private Executor executor;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deadline_on", nullable = false)
    private LocalDate deadlineOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Offering getOffering() {
        return offering;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        AWAITING_PAYMENT("Ожидание оплаты", false, true),
        REJECTED_PAYMENT("Не удалось произвести оплату", false, false),
        EXECUTING("Выполняется", true, true),
        COMPLETED("Выполнен", true, true);

        private final String name;
        private final boolean isPaid;
        private final boolean isAssignedWithExecutor;

        Status(String name, boolean isPaid, boolean isAssignedWithExecutor) {
            this.name = name;
            this.isPaid = isPaid;
            this.isAssignedWithExecutor = isAssignedWithExecutor;
        }

        public String getName() {
            return name;
        }

        public boolean isPaid() {
            return isPaid;
        }

        public boolean isAssignedWithExecutor() {
            return isAssignedWithExecutor;
        }
    }
}
