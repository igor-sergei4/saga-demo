package com.github.igorsergei4.sagademo.analytics.model.payment;

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
import java.time.LocalDateTime;

@Entity
@Table(name = "order_payment")
public class OrderPayment extends EntityWithIdProjection {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_payment_client")
    private PaymentClient client;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "status")
    private String status;

    public PaymentClient getClient() {
        return client;
    }

    public void setClient(PaymentClient client) {
        this.client = client;
    }

    public Order getOrderId() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
