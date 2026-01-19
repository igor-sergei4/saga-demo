package com.github.igorsergei4.sagademo.common.microservice.consumer;

import com.github.igorsergei4.sagademo.common.microservice.EventWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IdempotentEventProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdempotentEventProcessor.class);

    private final  ProcessedEventService processedEventService;

    public IdempotentEventProcessor(ProcessedEventService processedEventService) {
        this.processedEventService = processedEventService;
    }

    @Transactional(rollbackFor = Throwable.class)
    public <EventT> void processAnEvent(
            String topic,
            EventWrapper<EventT> eventWrapper,
            EventProcessor<EventT> eventProcessor,
            Class<EventT> eventClass
    ) {
        try {
            EventT orderCreatedEvent = processedEventService.getEventIfNotProcessed(
                    topic,
                    eventWrapper,
                    eventClass
            );
            if (orderCreatedEvent == null) {
                return;
            }

            eventProcessor.process(orderCreatedEvent);
        } catch (Throwable throwable) {
            LOGGER.error(throwable.getMessage(), throwable);
        }
    }
}
