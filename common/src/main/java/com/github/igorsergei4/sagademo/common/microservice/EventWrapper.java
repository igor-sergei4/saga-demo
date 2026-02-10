package com.github.igorsergei4.sagademo.common.microservice;

public class EventWrapper<EventT> {
    private Long eventId;
    private EventT event;

    public EventWrapper(Long eventId, EventT event) {
        this.eventId = eventId;
        this.event = event;
    }

    public EventWrapper() {
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public EventT getEvent() {
        return event;
    }

    public void setEvent(EventT event) {
        this.event = event;
    }
}
