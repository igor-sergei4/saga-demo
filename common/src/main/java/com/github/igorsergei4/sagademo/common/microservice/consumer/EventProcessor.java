package com.github.igorsergei4.sagademo.common.microservice.consumer;

public interface EventProcessor<EventT> {
    void process(EventT event);
}
