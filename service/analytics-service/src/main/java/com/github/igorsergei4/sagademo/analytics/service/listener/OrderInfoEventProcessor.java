package com.github.igorsergei4.sagademo.analytics.service.listener;

import com.github.igorsergei4.sagademo.analytics.service.entity.OrderService;
import com.github.igorsergei4.sagademo.common.microservice.consumer.EventProcessor;
import com.github.igorsergei4.sagademo.order.event.OrderInfoEvent;
import org.springframework.stereotype.Service;

@Service
public class OrderInfoEventProcessor implements EventProcessor<OrderInfoEvent> {
    private final OrderService orderService;

    public OrderInfoEventProcessor(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void process(OrderInfoEvent orderInfoEvent) {
        orderService.saveOrderInfo(orderInfoEvent);
    }
}
