package com.github.igorsergei4.sagademo.analytics.service.listener;

import com.github.igorsergei4.sagademo.analytics.service.entity.OrderPaymentService;
import com.github.igorsergei4.sagademo.common.microservice.consumer.EventProcessor;
import com.github.igorsergei4.sagademo.payment.event.OrderPaymentInfoEvent;
import org.springframework.stereotype.Service;

@Service
public class OrderPaymentInfoEventProcessor implements EventProcessor<OrderPaymentInfoEvent> {
    private final OrderPaymentService orderPaymentService;

    public OrderPaymentInfoEventProcessor(OrderPaymentService orderPaymentService) {
        this.orderPaymentService = orderPaymentService;
    }

    @Override
    public void process(OrderPaymentInfoEvent orderPaymentInfoEvent) {
        orderPaymentService.saveOrderPaymentInfo(orderPaymentInfoEvent);
    }
}
