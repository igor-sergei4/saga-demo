package com.github.igorsergei4.sagademo.common.microservice.producer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "queued_event")
public class QueuedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "topic", nullable = false, length = 100)
    private String topic;

    @Column(name = "key", nullable = false, length = 50)
    private String key;

    @Column(name = "payload_class_name", nullable = false)
    private String payloadClassName;

    @Column(name = "payload", nullable = false)
    private String payload;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_delivered")
    private boolean isDelivered;

    public QueuedEvent(String topic, String key, String payloadClassName, String payload) {
        this.topic = topic;
        this.key = key;
        this.payloadClassName = payloadClassName;;
        this.payload = payload;
        this.createdAt = LocalDateTime.now();
        this.isDelivered = false;
    }

    public QueuedEvent() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPayloadClassName() {
        return payloadClassName;
    }

    public void setPayloadClassName(String payloadClassName) {
        this.payloadClassName = payloadClassName;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean getIsDelivered() {
        return isDelivered;
    }

    public void setIsDelivered(boolean isDelivered) {
        this.isDelivered = isDelivered;
    }
}
