package com.github.igorsergei4.sagademo.payment.service.saga;

import com.github.igorsergei4.sagademo.common.microservice.EventWrapper;
import com.github.igorsergei4.sagademo.common.microservice.consumer.IdempotentEventProcessor;
import com.github.igorsergei4.sagademo.order.event.OrderRequiresPaymentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderSagaEventConsumer {
    private static final String ORDER_REQUIRES_PAYMENT_TOPIC = "order-requires-payment";

    private final IdempotentEventProcessor idempotentEventProcessor;
    private final OrderRequiresPaymentEventProcessor orderRequiresPaymentEventProcessor;

    public CreateOrderSagaEventConsumer(
            IdempotentEventProcessor idempotentEventProcessor,
            OrderRequiresPaymentEventProcessor orderRequiresPaymentEventProcessor
    ) {
        this.idempotentEventProcessor = idempotentEventProcessor;
        this.orderRequiresPaymentEventProcessor = orderRequiresPaymentEventProcessor;
    }

    @KafkaListener(topics = ORDER_REQUIRES_PAYMENT_TOPIC)
    public void handleOrderRequiresPaymentEvent(EventWrapper<OrderRequiresPaymentEvent> orderRequiresPaymentEventWrapper) {
        idempotentEventProcessor.processAnEvent(
                ORDER_REQUIRES_PAYMENT_TOPIC,
                orderRequiresPaymentEventWrapper,
                orderRequiresPaymentEventProcessor,
                OrderRequiresPaymentEvent.class
        );
    }
}
