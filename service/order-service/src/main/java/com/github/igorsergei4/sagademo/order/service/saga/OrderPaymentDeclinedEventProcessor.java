package com.github.igorsergei4.sagademo.order.service.saga;

import com.github.igorsergei4.sagademo.common.microservice.consumer.EventProcessor;
import com.github.igorsergei4.sagademo.common.microservice.producer.QueuedEventService;
import com.github.igorsergei4.sagademo.order.event.OrderExecutionNotPaidEvent;
import com.github.igorsergei4.sagademo.order.model.Order;
import com.github.igorsergei4.sagademo.order.service.analytics.AnalyticsServiceDataSender;
import com.github.igorsergei4.sagademo.order.service.entity.OrderService;
import com.github.igorsergei4.sagademo.payment.event.OrderPaymentDeclinedEvent;
import org.springframework.stereotype.Service;

@Service
public class OrderPaymentDeclinedEventProcessor implements EventProcessor<OrderPaymentDeclinedEvent> {
    private static final String ORDER_EXECUTION_NOT_PAID_TOPIC = "order-execution-not-paid";

    private final OrderService orderService;

    private final AnalyticsServiceDataSender analyticsServiceDataSender;

    private final QueuedEventService queuedEventService;

    public OrderPaymentDeclinedEventProcessor(
            OrderService orderService,
            AnalyticsServiceDataSender analyticsServiceDataSender,
            QueuedEventService queuedEventService
    ) {
        this.orderService = orderService;
        this.analyticsServiceDataSender = analyticsServiceDataSender;
        this.queuedEventService = queuedEventService;
    }

    @Override
    public void process(OrderPaymentDeclinedEvent orderPaymentDeclinedEvent) {
        Long orderId = orderPaymentDeclinedEvent.orderId();
        Order order = orderService.findById(orderId).orElseThrow(
                () -> orderService.getOrderDisappearedException(orderId)
        );

        if (!Order.Status.AWAITING_PAYMENT.equals(order.getStatus())) {
            throw new IllegalStateException(
                    String.format("Cannot reject an Order(id=%d) with status=%s for no payment.", orderId, order.getStatus())
            );
        }

        order.setStatus(Order.Status.REJECTED_PAYMENT);
        order = orderService.save(order);

        queuedEventService.queueAnEvent(
                ORDER_EXECUTION_NOT_PAID_TOPIC,
                orderId.toString(),
                new OrderExecutionNotPaidEvent(orderId)
        );
        analyticsServiceDataSender.provideOrderInfo(order);
    }
}
