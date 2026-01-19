package com.github.igorsergei4.sagademo.common.microservice.consumer;

import jakarta.persistence.Column;

import java.util.Objects;

public class ProcessedEventKey {
    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    public ProcessedEventKey(String topic, Long eventId) {
        this.topic = topic;
        this.eventId = eventId;
    }

    public ProcessedEventKey() {
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProcessedEventKey that = (ProcessedEventKey) o;
        return Objects.equals(topic, that.topic) && Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, eventId);
    }
}
