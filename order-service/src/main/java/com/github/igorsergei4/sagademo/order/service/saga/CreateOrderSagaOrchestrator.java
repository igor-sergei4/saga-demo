package com.github.igorsergei4.sagademo.order.service.saga;

import com.github.igorsergei4.sagademo.common.exception.EntityNotFoundException;
import com.github.igorsergei4.sagademo.common.microservice.EventWrapper;
import com.github.igorsergei4.sagademo.common.microservice.consumer.IdempotentEventProcessor;
import com.github.igorsergei4.sagademo.common.microservice.producer.QueuedEventService;
import com.github.igorsergei4.sagademo.execution.event.OrderExecutionRejectedEvent;
import com.github.igorsergei4.sagademo.execution.event.OrderExecutorAssignedEvent;
import com.github.igorsergei4.sagademo.order.dto.RegisterOrderParams;
import com.github.igorsergei4.sagademo.order.event.OrderCreatedEvent;
import com.github.igorsergei4.sagademo.order.model.Order;
import com.github.igorsergei4.sagademo.order.service.analytics.AnalyticsServiceDataSender;
import com.github.igorsergei4.sagademo.order.service.entity.OrderService;
import com.github.igorsergei4.sagademo.payment.event.OrderPaymentApprovedEvent;
import com.github.igorsergei4.sagademo.payment.event.OrderPaymentDeclinedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOrderSagaOrchestrator {
    private static final String ORDER_CREATED_TOPIC = "order-created";

    private static final String ORDER_EXECUTION_REJECTED_TOPIC = "order-execution-rejected";
    private static final String ORDER_EXECUTOR_ASSIGNED_TOPIC = "order-executor-assigned";

    private static final String ORDER_PAYMENT_APPROVED_TOPIC = "order-payment-approved";
    private static final String ORDER_PAYMENT_DECLINED_TOPIC = "order-payment-declined";

    private final OrderService orderService;

    private final OrderExecutorAssignedEventProcessor orderExecutorAssignedEventProcessor;
    private final OrderExecutionRejectedEventProcessor orderExecutionRejectedEventProcessor;

    private final OrderPaymentApprovedEventProcessor orderPaymentApprovedEventProcessor;
    private final OrderPaymentDeclinedEventProcessor orderPaymentDeclinedEventProcessor;

    private final AnalyticsServiceDataSender analyticsServiceDataSender;

    private final QueuedEventService queuedEventService;
    private final IdempotentEventProcessor idempotentEventProcessor;

    public CreateOrderSagaOrchestrator(
            OrderService orderService,
            OrderExecutorAssignedEventProcessor orderExecutorAssignedEventProcessor,
            OrderExecutionRejectedEventProcessor orderExecutionRejectedEventProcessor,
            OrderPaymentApprovedEventProcessor orderPaymentApprovedEventProcessor,
            OrderPaymentDeclinedEventProcessor orderPaymentDeclinedEventProcessor,
            AnalyticsServiceDataSender analyticsServiceDataSender,
            QueuedEventService queuedEventService,
            IdempotentEventProcessor idempotentEventProcessor
    ) {
        this.orderService = orderService;
        this.orderExecutorAssignedEventProcessor = orderExecutorAssignedEventProcessor;
        this.orderExecutionRejectedEventProcessor = orderExecutionRejectedEventProcessor;
        this.orderPaymentApprovedEventProcessor = orderPaymentApprovedEventProcessor;
        this.orderPaymentDeclinedEventProcessor = orderPaymentDeclinedEventProcessor;
        this.analyticsServiceDataSender = analyticsServiceDataSender;
        this.queuedEventService = queuedEventService;
        this.idempotentEventProcessor = idempotentEventProcessor;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void registerOrder(RegisterOrderParams registerOrderParams) throws EntityNotFoundException {
        Order order = orderService.createOrder(registerOrderParams);
        queuedEventService.queueAnEvent(
                ORDER_CREATED_TOPIC,
                order.getId().toString(),
                new OrderCreatedEvent(order)
        );
        analyticsServiceDataSender.provideOrderInfo(order);
    }

    @KafkaListener(topics = ORDER_EXECUTOR_ASSIGNED_TOPIC)
    public void processOrderExecutionRejectedEvent(
            EventWrapper<OrderExecutorAssignedEvent> orderExecutorAssignedEventWrapper
    ) {
        idempotentEventProcessor.processAnEvent(
                ORDER_EXECUTOR_ASSIGNED_TOPIC,
                orderExecutorAssignedEventWrapper,
                orderExecutorAssignedEventProcessor,
                OrderExecutorAssignedEvent.class
        );
    }

    @KafkaListener(topics = ORDER_EXECUTION_REJECTED_TOPIC)
    public void processOrderExecutorAssignedEvent(
            EventWrapper<OrderExecutionRejectedEvent> orderExecutionRejectedEventWrapper
    ) {
        idempotentEventProcessor.processAnEvent(
                ORDER_EXECUTION_REJECTED_TOPIC,
                orderExecutionRejectedEventWrapper,
                orderExecutionRejectedEventProcessor,
                OrderExecutionRejectedEvent.class
        );
    }

    @KafkaListener(topics = ORDER_PAYMENT_APPROVED_TOPIC)
    public void processOrderPaymentApprovedEvent(
            EventWrapper<OrderPaymentApprovedEvent> orderPaymentApprovedEventWrapper
    ) {
        idempotentEventProcessor.processAnEvent(
                ORDER_PAYMENT_APPROVED_TOPIC,
                orderPaymentApprovedEventWrapper,
                orderPaymentApprovedEventProcessor,
                OrderPaymentApprovedEvent.class
        );
    }

    @KafkaListener(topics = ORDER_PAYMENT_DECLINED_TOPIC)
    public void processOrderPaymentDeclinedEvent(
            EventWrapper<OrderPaymentDeclinedEvent> orderPaymentDeclinedEventWrapper
    ) {
        idempotentEventProcessor.processAnEvent(
                ORDER_PAYMENT_DECLINED_TOPIC,
                orderPaymentDeclinedEventWrapper,
                orderPaymentDeclinedEventProcessor,
                OrderPaymentDeclinedEvent.class
        );
    }
}
