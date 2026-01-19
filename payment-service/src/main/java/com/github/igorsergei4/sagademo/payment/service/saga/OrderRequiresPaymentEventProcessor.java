package com.github.igorsergei4.sagademo.payment.service.saga;

import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;
import com.github.igorsergei4.sagademo.common.microservice.consumer.EventProcessor;
import com.github.igorsergei4.sagademo.common.microservice.producer.QueuedEventService;
import com.github.igorsergei4.sagademo.order.event.OrderRequiresPaymentEvent;
import com.github.igorsergei4.sagademo.payment.event.OrderPaymentApprovedEvent;
import com.github.igorsergei4.sagademo.payment.event.OrderPaymentDeclinedEvent;
import com.github.igorsergei4.sagademo.payment.model.Client;
import com.github.igorsergei4.sagademo.payment.model.OrderPayment;
import com.github.igorsergei4.sagademo.payment.service.analytics.AnalyticsServiceDataSender;
import com.github.igorsergei4.sagademo.payment.service.entity.ClientService;
import com.github.igorsergei4.sagademo.payment.service.entity.OrderPaymentService;
import com.github.igorsergei4.sagademo.payment.service.external.PaymentSystemService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderRequiresPaymentEventProcessor implements EventProcessor<OrderRequiresPaymentEvent> {
    private static final String ORDER_PAYMENT_APPROVED_TOPIC = "order-payment-approved";
    private static final String ORDER_PAYMENT_DECLINED_TOPIC = "order-payment-declined";

    private final ClientService clientService;
    private final OrderPaymentService orderPaymentService;

    private final PaymentSystemService  paymentSystemService;
    private final AnalyticsServiceDataSender analyticsServiceDataSender;

    private final QueuedEventService queuedEventService;

    public OrderRequiresPaymentEventProcessor(
            ClientService clientService,
            OrderPaymentService orderPaymentService,
            PaymentSystemService paymentSystemService,
            AnalyticsServiceDataSender analyticsServiceDataSender,
            QueuedEventService queuedEventService
    ) {
        this.clientService = clientService;
        this.orderPaymentService = orderPaymentService;
        this.paymentSystemService = paymentSystemService;
        this.analyticsServiceDataSender = analyticsServiceDataSender;
        this.queuedEventService = queuedEventService;
    }

    @Override
    public void process(OrderRequiresPaymentEvent event) {
        OrderPayment orderPayment = initOrderPayment(
                event.orderId(),
                getClient(event.client()),
                event.cost()
        );

        Long orderId = orderPayment.getOrderId();;
        if (OrderPayment.Status.COMMITED.equals(orderPayment.getStatus())) {
            queuedEventService.queueAnEvent(
                    ORDER_PAYMENT_APPROVED_TOPIC,
                    orderId.toString(),
                    new OrderPaymentApprovedEvent(orderId)
            );
        } else {
            queuedEventService.queueAnEvent(
                    ORDER_PAYMENT_DECLINED_TOPIC,
                    orderId.toString(),
                    new OrderPaymentDeclinedEvent(orderId)
            );
        }

        analyticsServiceDataSender.provideOrderPaymentInfo(orderPayment);
    }

    private Client getClient(NamedEntityDto remoteClientDto) {
        Long clientId = remoteClientDto.getId();
        String clientName = remoteClientDto.getName();
        return clientService.findByRemoteId(clientId)
                .map(localClient -> {
                    localClient.setName(clientName);
                    return localClient;
                })
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setRemoteId(clientId);
                    newClient.setName(clientName);
                    return clientService.save(newClient);
                });
    }

    private OrderPayment initOrderPayment(Long orderId, Client client, BigDecimal cost) {
        if (orderPaymentService.findByOrderId(orderId).isPresent()) {
            throw new IllegalStateException("Payment for order with id=" + orderId + " was already processed.");
        }

        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setOrderId(orderId);
        orderPayment.setClient(client);
        orderPayment.setCost(cost);
        paymentSystemService.commitPaymentAndGetPaymentId(client, cost).ifPresentOrElse(
                (paymentId) -> {
                    orderPayment.setStatus(OrderPayment.Status.COMMITED);
                    orderPayment.setPaymentId(paymentId);
                },
                () -> {
                    orderPayment.setStatus(OrderPayment.Status.REJECTED);
                }
        );
        orderPayment.setProcessedAt(LocalDateTime.now());

        return orderPaymentService.save(orderPayment);
    }
}
