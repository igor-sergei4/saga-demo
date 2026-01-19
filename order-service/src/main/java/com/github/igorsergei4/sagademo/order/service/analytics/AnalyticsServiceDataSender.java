package com.github.igorsergei4.sagademo.order.service.analytics;

import com.github.igorsergei4.sagademo.common.microservice.producer.QueuedEventService;
import com.github.igorsergei4.sagademo.order.event.OrderInfoEvent;
import com.github.igorsergei4.sagademo.order.model.Order;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceDataSender {
    private final QueuedEventService queuedEventService;

    public AnalyticsServiceDataSender(QueuedEventService queuedEventService) {
        this.queuedEventService = queuedEventService;
    }

    public void provideOrderInfo(Order order) {
        queuedEventService.queueAnEvent(
                OrderInfoEvent.TOPIC,
                order.getId().toString(),
                new OrderInfoEvent(order)
        );
    }
}
