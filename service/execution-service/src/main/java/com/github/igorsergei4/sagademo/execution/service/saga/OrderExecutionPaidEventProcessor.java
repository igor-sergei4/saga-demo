package com.github.igorsergei4.sagademo.execution.service.saga;

import com.github.igorsergei4.sagademo.common.microservice.consumer.EventProcessor;
import com.github.igorsergei4.sagademo.execution.model.Execution;
import com.github.igorsergei4.sagademo.execution.service.analytics.AnalyticsServiceDataSender;
import com.github.igorsergei4.sagademo.execution.service.entity.ExecutionService;
import com.github.igorsergei4.sagademo.order.event.OrderExecutionPaidEvent;
import org.springframework.stereotype.Service;

@Service
public class OrderExecutionPaidEventProcessor implements EventProcessor<OrderExecutionPaidEvent> {
    private final ExecutionService executionService;

    private final AnalyticsServiceDataSender analyticsServiceDataSender;

    public OrderExecutionPaidEventProcessor(
            ExecutionService executionService,
            AnalyticsServiceDataSender analyticsServiceDataSender
    ) {
        this.executionService = executionService;
        this.analyticsServiceDataSender = analyticsServiceDataSender;
    }

    @Override
    public void process(OrderExecutionPaidEvent orderExecutionPaidEvent) {
        Long orderId = orderExecutionPaidEvent.orderId();
        Execution execution = executionService.findByOrderId(orderId).orElseThrow(
                () -> new IllegalStateException("No execution is assigned for paid order with id=" + orderId)
        );

        Execution.Status executionStatus = execution.getStatus();
        if (executionStatus.isPaid()) {
            return;
        } else if (!executionStatus.equals(Execution.Status.AWAITING_PAYMENT)) {
            throw new IllegalStateException(
                    String.format("Cannot confirm payment for Execution(id=%d) with status=%s.", execution.getId(), executionStatus)
            );
        }

        execution.setStatus(Execution.Status.EXECUTING);
        execution = executionService.save(execution);

        analyticsServiceDataSender.provideExecutionInfo(execution);
    }
}
