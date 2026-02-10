package com.github.igorsergei4.sagademo.order.service.saga;

import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;
import com.github.igorsergei4.sagademo.common.microservice.consumer.EventProcessor;
import com.github.igorsergei4.sagademo.common.microservice.producer.QueuedEventService;
import com.github.igorsergei4.sagademo.execution.event.OrderExecutorAssignedEvent;
import com.github.igorsergei4.sagademo.order.event.OrderRequiresPaymentEvent;
import com.github.igorsergei4.sagademo.order.model.Executor;
import com.github.igorsergei4.sagademo.order.model.Order;
import com.github.igorsergei4.sagademo.order.service.analytics.AnalyticsServiceDataSender;
import com.github.igorsergei4.sagademo.order.service.entity.ExecutorService;
import com.github.igorsergei4.sagademo.order.service.entity.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderExecutorAssignedEventProcessor implements EventProcessor<OrderExecutorAssignedEvent> {
    private static final String ORDER_REQUIRES_PAYMENT_TOPIC = "order-requires-payment";

    private final OrderService orderService;
    private final ExecutorService executorService;

    private final AnalyticsServiceDataSender analyticsServiceDataSender;

    private final QueuedEventService queuedEventService;

    public OrderExecutorAssignedEventProcessor(
            OrderService orderService, ExecutorService executorService, AnalyticsServiceDataSender analyticsServiceDataSender,
            QueuedEventService queuedEventService
    ) {
        this.orderService = orderService;
        this.executorService = executorService;
        this.analyticsServiceDataSender = analyticsServiceDataSender;
        this.queuedEventService = queuedEventService;
    }

    @Override
    public void process(OrderExecutorAssignedEvent event) {
        Long orderId = event.orderId();
        Order order = orderService.findById(orderId).orElseThrow(() ->
                orderService.getOrderDisappearedException(orderId)
        );
        assignExecutor(order, getExecutor(event.executor()));
    }

    private Executor getExecutor(NamedEntityDto executorDto) {
        Long executorId = executorDto.getId();
        Executor executor = executorService.findByRemoteId(executorDto.getId())
                .orElseGet(() -> {
                    Executor newExecutor = new Executor();
                    newExecutor.setRemoteId(executorId);
                    return newExecutor;
                });

        executor.setName(executorDto.getName());
        return executorService.save(executor);
    }

    private void assignExecutor(Order order, Executor executor) {
        Long orderId = order.getId();
        if (!order.getStatus().equals(Order.Status.AWAITING_EXECUTOR)) {
            throw new IllegalStateException("Order with id=" + orderId + " is no more awaiting executor!");
        }

        order.setExecutor(executor);
        order.setStatus(Order.Status.AWAITING_PAYMENT);
        order = orderService.save(order);

        queuedEventService.queueAnEvent(
                ORDER_REQUIRES_PAYMENT_TOPIC,
                orderId.toString(),
                new OrderRequiresPaymentEvent(
                        orderId,
                        order.getCost(),
                        new NamedEntityDto(order.getClient())
                )
        );
        analyticsServiceDataSender.provideOrderInfo(order);
    }
}
