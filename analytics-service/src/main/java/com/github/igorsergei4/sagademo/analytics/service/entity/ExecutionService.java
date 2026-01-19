package com.github.igorsergei4.sagademo.analytics.service.entity;

import com.github.igorsergei4.sagademo.analytics.model.execution.Execution;
import com.github.igorsergei4.sagademo.analytics.model.execution.ExecutionClient;
import com.github.igorsergei4.sagademo.analytics.model.execution.ExecutionExecutor;
import com.github.igorsergei4.sagademo.analytics.model.execution.ExecutionOffering;
import com.github.igorsergei4.sagademo.analytics.repository.execution.ExecutionClientRepository;
import com.github.igorsergei4.sagademo.analytics.repository.execution.ExecutionExecutorRepository;
import com.github.igorsergei4.sagademo.analytics.repository.execution.ExecutionOfferingRepository;
import com.github.igorsergei4.sagademo.analytics.repository.execution.ExecutionRepository;
import com.github.igorsergei4.sagademo.execution.event.ExecutionInfoEvent;
import org.springframework.stereotype.Service;

@Service
public class ExecutionService extends EntityWithIdProjectionService {
    private final ExecutionRepository executionRepository;
    private final ExecutionClientRepository executionClientRepository;
    private final ExecutionOfferingRepository executionOfferingRepository;
    private final ExecutionExecutorRepository executionExecutorRepository;

    private final OrderService orderService;

    public ExecutionService(
            ExecutionRepository executionRepository,
            ExecutionClientRepository executionClientRepository,
            ExecutionOfferingRepository executionOfferingRepository,
            ExecutionExecutorRepository executionExecutorRepository,
            OrderService orderService
    ) {
        this.executionRepository = executionRepository;
        this.executionClientRepository = executionClientRepository;
        this.executionOfferingRepository = executionOfferingRepository;
        this.executionExecutorRepository = executionExecutorRepository;
        this.orderService = orderService;
    }

    public void saveExecutionInfo(ExecutionInfoEvent executionInfoEvent) {
        saveInfo(executionRepository, executionInfoEvent, this::mapExecutionProperties, Execution::new);
    }

    private void mapExecutionProperties(Execution execution, ExecutionInfoEvent executionInfoEvent) {
        execution.setClient(saveInfo(executionClientRepository, executionInfoEvent.client(), this::mapNamedEntityProperties, ExecutionClient::new));
        execution.setOrder(orderService.getProxy(executionInfoEvent.orderId()));
        execution.setOffering(saveInfo(executionOfferingRepository, executionInfoEvent.offering(), this::mapOfferingProperties, ExecutionOffering::new));
        execution.setCost(executionInfoEvent.cost());
        execution.setExecutor(saveInfo(executionExecutorRepository, executionInfoEvent.executor(), this::mapExecutorProperties, ExecutionExecutor::new));
        execution.setCreatedAt(executionInfoEvent.createdAt());
        execution.setDeadlineOn(executionInfoEvent.deadlineOn());
        execution.setStatus(executionInfoEvent.status());
    }

    private void mapOfferingProperties(ExecutionOffering executionOffering, ExecutionInfoEvent.OfferingDto offeringDto) {
        mapNamedEntityProperties(executionOffering, offeringDto);
        executionOffering.setCost(offeringDto.getCost());
        executionOffering.setPoints(offeringDto.getPoints());
        executionOffering.setDeprecatedAt(offeringDto.getDeprecatedAt());
    }

    private void mapExecutorProperties(ExecutionExecutor executionExecutor, ExecutionInfoEvent.ExecutorDto executorDto) {
        mapNamedEntityProperties(executionExecutor, executorDto);
        executionExecutor.setDailyPoints(executorDto.getDailyPoints());
        executionExecutor.setFinalDate(executorDto.getFinalDate());
    }
}
