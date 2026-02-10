package com.github.igorsergei4.sagademo.execution.service.saga;

import com.github.igorsergei4.sagademo.common.microservice.EventWrapper;
import com.github.igorsergei4.sagademo.common.microservice.consumer.IdempotentEventProcessor;
import com.github.igorsergei4.sagademo.order.event.OrderCreatedEvent;
import com.github.igorsergei4.sagademo.order.event.OrderExecutionNotPaidEvent;
import com.github.igorsergei4.sagademo.order.event.OrderExecutionPaidEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderSagaEventConsumer {
    private static final String ORDER_CREATED_TOPIC = "order-created";

    private static final String ORDER_EXECUTION_PAID_TOPIC = "order-execution-paid";
    private static final String ORDER_EXECUTION_NOT_PAID_TOPIC = "order-execution-not-paid";

    private final OrderCreatedEventProcessor orderCreatedEventProcessor;

    private final OrderExecutionPaidEventProcessor orderExecutionPaidEventProcessor;
    private final OrderExecutionNotPaidEventProcessor orderExecutionNotPaidEventProcessor;

    private final IdempotentEventProcessor idempotentEventProcessor;

    public CreateOrderSagaEventConsumer(
            OrderCreatedEventProcessor orderCreatedEventProcessor,
            OrderExecutionPaidEventProcessor orderExecutionPaidEventProcessor,
            OrderExecutionNotPaidEventProcessor orderExecutionNotPaidEventProcessor,
            IdempotentEventProcessor idempotentEventProcessor
    ) {
        this.orderCreatedEventProcessor = orderCreatedEventProcessor;
        this.orderExecutionPaidEventProcessor = orderExecutionPaidEventProcessor;
        this.orderExecutionNotPaidEventProcessor = orderExecutionNotPaidEventProcessor;
        this.idempotentEventProcessor = idempotentEventProcessor;
    }

    @KafkaListener(topics = ORDER_CREATED_TOPIC)
    public void handleOrderCreatedEvent(EventWrapper<OrderCreatedEvent> orderCreatedEventWrapper) {
        idempotentEventProcessor.processAnEvent(
                ORDER_CREATED_TOPIC,
                orderCreatedEventWrapper,
                orderCreatedEventProcessor,
                OrderCreatedEvent.class
        );
    }

    @KafkaListener(topics = ORDER_EXECUTION_PAID_TOPIC)
    public void handleOrderExecutionPaidEvent(EventWrapper<OrderExecutionPaidEvent> orderExecutionPaidEventWrapper) {
        idempotentEventProcessor.processAnEvent(
                ORDER_EXECUTION_PAID_TOPIC,
                orderExecutionPaidEventWrapper,
                orderExecutionPaidEventProcessor,
                OrderExecutionPaidEvent.class
        );
    }

    @KafkaListener(topics = ORDER_EXECUTION_NOT_PAID_TOPIC)
    public void handleOrderExecutionNotPaidEvent(EventWrapper<OrderExecutionNotPaidEvent> orderExecutionNotPaidEventWrapper) {
        idempotentEventProcessor.processAnEvent(
                ORDER_EXECUTION_NOT_PAID_TOPIC,
                orderExecutionNotPaidEventWrapper,
                orderExecutionNotPaidEventProcessor,
                OrderExecutionNotPaidEvent.class
        );
    }
}
