package com.github.igorsergei4.sagademo.execution.service.saga;

import com.github.igorsergei4.sagademo.common.microservice.consumer.EventProcessor;
import com.github.igorsergei4.sagademo.execution.model.Execution;
import com.github.igorsergei4.sagademo.execution.service.analytics.AnalyticsServiceDataSender;
import com.github.igorsergei4.sagademo.execution.service.entity.ExecutionService;
import com.github.igorsergei4.sagademo.order.event.OrderExecutionNotPaidEvent;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderExecutionNotPaidEventProcessor implements EventProcessor<OrderExecutionNotPaidEvent> {
    private final ExecutionService executionService;

    private final AnalyticsServiceDataSender analyticsServiceDataSender;

    public OrderExecutionNotPaidEventProcessor(
            ExecutionService executionService,
            AnalyticsServiceDataSender analyticsServiceDataSender
    ) {
        this.executionService = executionService;
        this.analyticsServiceDataSender = analyticsServiceDataSender;
    }

    @Override
    public void process(OrderExecutionNotPaidEvent orderExecutionNotPaidEvent) {
        Long orderId = orderExecutionNotPaidEvent.orderId();
        Optional<Execution> executionOptional = executionService.findByOrderId(orderId);
        if (executionOptional.isEmpty()) {
            return;
        }

        Execution execution = executionOptional.get();
        Execution.Status executionStatus = execution.getStatus();
        if (executionStatus.isPaid()) {
            throw new IllegalStateException(
                    String.format("Paid Execution(id=%d) with status=%s was marked as not paid.", execution.getId(), executionStatus)
            );
        }

        execution = executionService.cancelExecution(execution, Execution.Status.REJECTED_PAYMENT);

        analyticsServiceDataSender.provideExecutionInfo(execution);
    }
}
