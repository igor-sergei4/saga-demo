package com.github.igorsergei4.sagademo.analytics.service.entity;

import com.github.igorsergei4.sagademo.analytics.model.payment.OrderPayment;
import com.github.igorsergei4.sagademo.analytics.model.payment.PaymentClient;
import com.github.igorsergei4.sagademo.analytics.repository.payment.OrderPaymentRepository;
import com.github.igorsergei4.sagademo.analytics.repository.payment.PaymentClientRepository;
import com.github.igorsergei4.sagademo.payment.event.OrderPaymentInfoEvent;
import org.springframework.stereotype.Service;

@Service
public class OrderPaymentService extends EntityWithIdProjectionService {
    private final OrderPaymentRepository orderPaymentRepository;
    private final PaymentClientRepository paymentClientRepository;

    private final OrderService orderService;

    public OrderPaymentService(
            OrderPaymentRepository orderPaymentRepository,
            PaymentClientRepository paymentClientRepository,
            OrderService orderService
    ) {
        this.orderPaymentRepository = orderPaymentRepository;
        this.paymentClientRepository = paymentClientRepository;
        this.orderService = orderService;
    }

    public void saveOrderPaymentInfo(OrderPaymentInfoEvent orderPaymentInfoEvent) {
        saveInfo(orderPaymentRepository, orderPaymentInfoEvent, this::mapOrderPaymentProperties, OrderPayment::new);
    }

    private void mapOrderPaymentProperties(OrderPayment orderPayment, OrderPaymentInfoEvent orderPaymentInfoEvent) {
        orderPayment.setClient(saveInfo(paymentClientRepository, orderPaymentInfoEvent.client(), this::mapNamedEntityProperties, PaymentClient::new));
        orderPayment.setOrder(orderService.getProxy(orderPaymentInfoEvent.orderId()));
        orderPayment.setPaymentId(orderPaymentInfoEvent.paymentId());
        orderPayment.setCost(orderPaymentInfoEvent.cost());
        orderPayment.setProcessedAt(orderPaymentInfoEvent.processedAt());
        orderPayment.setStatus(orderPaymentInfoEvent.status());
    }
}
