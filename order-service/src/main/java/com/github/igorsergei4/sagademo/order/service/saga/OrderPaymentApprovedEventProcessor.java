package com.github.igorsergei4.sagademo.order.service.saga;

import com.github.igorsergei4.sagademo.common.microservice.consumer.EventProcessor;
import com.github.igorsergei4.sagademo.common.microservice.producer.QueuedEventService;
import com.github.igorsergei4.sagademo.order.event.OrderExecutionPaidEvent;
import com.github.igorsergei4.sagademo.order.model.Order;
import com.github.igorsergei4.sagademo.order.service.analytics.AnalyticsServiceDataSender;
import com.github.igorsergei4.sagademo.order.service.entity.OrderService;
import com.github.igorsergei4.sagademo.payment.event.OrderPaymentApprovedEvent;
import org.springframework.stereotype.Service;

@Service
public class OrderPaymentApprovedEventProcessor implements EventProcessor<OrderPaymentApprovedEvent> {
    private static final String ORDER_EXECUTION_PAID_TOPIC = "order-execution-paid";

    private final OrderService orderService;

    private final AnalyticsServiceDataSender analyticsServiceDataSender;

    private final QueuedEventService queuedEventService;

    public OrderPaymentApprovedEventProcessor(
            OrderService orderService,
            AnalyticsServiceDataSender analyticsServiceDataSender,
            QueuedEventService queuedEventService
    ) {
        this.orderService = orderService;
        this.analyticsServiceDataSender = analyticsServiceDataSender;
        this.queuedEventService = queuedEventService;
    }

    @Override
    public void process(OrderPaymentApprovedEvent orderPaymentApprovedEvent) {
        Long orderId = orderPaymentApprovedEvent.orderId();
        Order order = orderService.findById(orderId).orElseThrow(
                () -> orderService.getOrderDisappearedException(orderId)
        );

        if (!Order.Status.AWAITING_PAYMENT.equals(order.getStatus())) {
            throw new IllegalStateException(
                    String.format("Cannot confirm payment for Order(id=%d) with status=%s.", orderId, order.getStatus())
            );
        }

        order.setStatus(Order.Status.EXECUTING);
        order = orderService.save(order);

        queuedEventService.queueAnEvent(
                ORDER_EXECUTION_PAID_TOPIC,
                orderId.toString(),
                new OrderExecutionPaidEvent(orderId)
        );
        analyticsServiceDataSender.provideOrderInfo(order);
    }
}
