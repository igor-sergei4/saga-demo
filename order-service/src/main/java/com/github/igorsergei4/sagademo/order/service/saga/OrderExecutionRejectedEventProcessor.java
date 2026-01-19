package com.github.igorsergei4.sagademo.order.service.saga;

import com.github.igorsergei4.sagademo.common.microservice.consumer.EventProcessor;
import com.github.igorsergei4.sagademo.common.microservice.producer.QueuedEventService;
import com.github.igorsergei4.sagademo.execution.event.OrderExecutionRejectedEvent;
import com.github.igorsergei4.sagademo.order.event.OrderInfoEvent;
import com.github.igorsergei4.sagademo.order.model.Order;
import com.github.igorsergei4.sagademo.order.service.analytics.AnalyticsServiceDataSender;
import com.github.igorsergei4.sagademo.order.service.entity.OfferingService;
import com.github.igorsergei4.sagademo.order.service.entity.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderExecutionRejectedEventProcessor implements EventProcessor<OrderExecutionRejectedEvent> {
    private final OrderService orderService;
    private final OfferingService offeringService;

    private final AnalyticsServiceDataSender analyticsServiceDataSender;

    public OrderExecutionRejectedEventProcessor(
            OrderService orderService,
            OfferingService offeringService,
            AnalyticsServiceDataSender analyticsServiceDataSender
    ) {
        this.orderService = orderService;
        this.offeringService = offeringService;
        this.analyticsServiceDataSender = analyticsServiceDataSender;
    }

    @Override
    public void process(OrderExecutionRejectedEvent orderExecutionRejectedEvent) {
        Long orderId = orderExecutionRejectedEvent.orderId();
        switch (orderExecutionRejectedEvent.rejectionReason()){
            case NO_SUCH_OFFERING -> processNoSuchOfferingEvent(
                    orderId,
                    orderExecutionRejectedEvent.actualizedOffering().getId()
            );
            case OFFERING_EXPIRED -> processOfferingExpiredEvent(
                    orderId,
                    orderExecutionRejectedEvent.actualizedOffering()
            );
            case NO_EXECUTORS -> rejectOrder(
                    orderId,
                    Order.Status.REJECTED_EXECUTOR
            );
        }
    }

    private void processNoSuchOfferingEvent(Long orderId, Long remoteOfferingId) {
        rejectOrder(orderId, Order.Status.REJECTED_OFFERING);

        offeringService.findByRemoteId(remoteOfferingId).ifPresent(offering -> {
            offering.setIsDeprecated(true);
            offeringService.save(offering);
        });
    }

    private void processOfferingExpiredEvent(Long orderId, OrderExecutionRejectedEvent.OfferingDto remoteOfferingDto) {
        rejectOrder(orderId, Order.Status.REJECTED_OFFERING);

        offeringService.findByRemoteId(remoteOfferingDto.getId()).ifPresent(offering -> {
            offering.setName(remoteOfferingDto.getName());
            offering.setCost(remoteOfferingDto.getCost());
            offering.setIsDeprecated(remoteOfferingDto.getIsDeprecated());

            offeringService.save(offering);
        });
    }

    private void rejectOrder(Long orderId, Order.Status statusToSet) {
        orderService.findById(orderId).ifPresent(order -> {
            if (!order.getStatus().equals(Order.Status.AWAITING_EXECUTOR)) {
                throw new IllegalStateException("Order with id=" + orderId +" is no more awaiting executor!");
            }

            order.setStatus(statusToSet);
            order = orderService.save(order);

            analyticsServiceDataSender.provideOrderInfo(order);
        });
    }
}
