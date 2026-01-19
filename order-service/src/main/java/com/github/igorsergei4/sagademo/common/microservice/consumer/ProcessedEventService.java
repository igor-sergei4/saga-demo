package com.github.igorsergei4.sagademo.common.microservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.igorsergei4.sagademo.common.microservice.EventWrapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ProcessedEventService {
    private final ObjectMapper customObjectMapper;

    private final ProcessedEventRepository processedEventRepository;

    public ProcessedEventService(
            ObjectMapper customObjectMapper,
            ProcessedEventRepository processedEventRepository
    ) {
        this.customObjectMapper = customObjectMapper;
        this.processedEventRepository = processedEventRepository;
    }

    public <EventT> EventT getEventIfNotProcessed(
            String topic,
            EventWrapper<EventT> eventWrapper,
            Class<EventT> eventClass)
    {
        ProcessedEventKey processedEventKey = new ProcessedEventKey(topic, eventWrapper.getEventId());
        if (processedEventRepository.existsById(processedEventKey)) {
            return null;
        }

        ProcessedEvent processedEvent = new ProcessedEvent();
        processedEvent.setId(processedEventKey);
        processedEvent.setProcessedAt(LocalDateTime.now());

        try {
            processedEventRepository.save(processedEvent);
        } catch (Throwable throwable) {
            throw throwable;
        }

        if (eventWrapper.getEvent() instanceof Map<?, ?>) {// TODO: Generic field is deserialized as a key-value map...
            return customObjectMapper.convertValue(eventWrapper.getEvent(), eventClass);
        }
        return eventWrapper.getEvent();
    }
}
