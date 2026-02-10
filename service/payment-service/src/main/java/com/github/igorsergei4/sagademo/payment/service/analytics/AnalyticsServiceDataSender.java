package com.github.igorsergei4.sagademo.payment.service.analytics;

import com.github.igorsergei4.sagademo.common.microservice.producer.QueuedEventService;
import com.github.igorsergei4.sagademo.payment.event.OrderPaymentInfoEvent;
import com.github.igorsergei4.sagademo.payment.model.OrderPayment;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceDataSender {
    private final QueuedEventService queuedEventService;

    public AnalyticsServiceDataSender(QueuedEventService queuedEventService) {
        this.queuedEventService = queuedEventService;
    }

    public void provideOrderPaymentInfo(OrderPayment orderPayment) {
        queuedEventService.queueAnEvent(
                OrderPaymentInfoEvent.TOPIC,
                orderPayment.getId().toString(),
                new OrderPaymentInfoEvent(orderPayment)
        );
    }
}
