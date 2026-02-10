package com.github.igorsergei4.sagademo.execution.service.analytics;

import com.github.igorsergei4.sagademo.common.microservice.producer.QueuedEventService;
import com.github.igorsergei4.sagademo.execution.event.ExecutionInfoEvent;
import com.github.igorsergei4.sagademo.execution.model.Execution;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceDataSender {
    private final QueuedEventService queuedEventService;

    public AnalyticsServiceDataSender(QueuedEventService queuedEventService) {
        this.queuedEventService = queuedEventService;
    }

    public void provideExecutionInfo(Execution execution) {
        queuedEventService.queueAnEvent(
                ExecutionInfoEvent.TOPIC,
                execution.getId().toString(),
                new ExecutionInfoEvent(execution)
        );
    }
}
