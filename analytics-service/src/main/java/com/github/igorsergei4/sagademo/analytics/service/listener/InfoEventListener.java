package com.github.igorsergei4.sagademo.analytics.service.listener;

import com.github.igorsergei4.sagademo.common.microservice.EventWrapper;
import com.github.igorsergei4.sagademo.common.microservice.consumer.IdempotentEventProcessor;
import com.github.igorsergei4.sagademo.execution.event.ExecutionInfoEvent;
import com.github.igorsergei4.sagademo.order.event.OrderInfoEvent;
import com.github.igorsergei4.sagademo.payment.event.OrderPaymentInfoEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InfoEventListener {
    private final OrderInfoEventProcessor orderInfoEventProcessor;
    private final ExecutionInfoEventProcessor executionInfoEventProcessor;
    private final OrderPaymentInfoEventProcessor orderPaymentInfoEventProcessor;

    private final IdempotentEventProcessor idempotentEventProcessor;

    public InfoEventListener(
            OrderInfoEventProcessor orderInfoEventProcessor,
            ExecutionInfoEventProcessor executionInfoEventProcessor,
            OrderPaymentInfoEventProcessor orderPaymentInfoEventProcessor, IdempotentEventProcessor idempotentEventProcessor
    ) {
        this.orderInfoEventProcessor = orderInfoEventProcessor;
        this.executionInfoEventProcessor = executionInfoEventProcessor;
        this.orderPaymentInfoEventProcessor = orderPaymentInfoEventProcessor;
        this.idempotentEventProcessor = idempotentEventProcessor;
    }

    @KafkaListener(topics = OrderInfoEvent.TOPIC)
    public void saveOrderInfo(EventWrapper<OrderInfoEvent> orderInfoEventWrapper) {
        idempotentEventProcessor.processAnEvent(
                OrderInfoEvent.TOPIC,
                orderInfoEventWrapper,
                orderInfoEventProcessor,
                OrderInfoEvent.class
        );
    }

    @KafkaListener(topics = ExecutionInfoEvent.TOPIC)
    public void saveExecutionInfo(EventWrapper<ExecutionInfoEvent> executionInfoEventWrapper) {
        idempotentEventProcessor.processAnEvent(
                ExecutionInfoEvent.TOPIC,
                executionInfoEventWrapper,
                executionInfoEventProcessor,
                ExecutionInfoEvent.class
        );
    }

    @KafkaListener(topics = OrderPaymentInfoEvent.TOPIC)
    public void saveOrderPaymentInfo(EventWrapper<OrderPaymentInfoEvent> orderPaymentInfoEventWrapper) {
        idempotentEventProcessor.processAnEvent(
                OrderPaymentInfoEvent.TOPIC,
                orderPaymentInfoEventWrapper,
                orderPaymentInfoEventProcessor,
                OrderPaymentInfoEvent.class
        );
    }
}
