package com.github.igorsergei4.sagademo.analytics.service.listener;

import com.github.igorsergei4.sagademo.analytics.service.entity.ExecutionService;
import com.github.igorsergei4.sagademo.common.microservice.consumer.EventProcessor;
import com.github.igorsergei4.sagademo.execution.event.ExecutionInfoEvent;
import org.springframework.stereotype.Service;

@Service
public class ExecutionInfoEventProcessor implements EventProcessor<ExecutionInfoEvent> {
    private final ExecutionService executionService;

    public ExecutionInfoEventProcessor(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @Override
    public void process(ExecutionInfoEvent executionInfoEvent) {
        executionService.saveExecutionInfo(executionInfoEvent);
    }
}
