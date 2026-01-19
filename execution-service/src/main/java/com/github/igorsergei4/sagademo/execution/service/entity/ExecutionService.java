package com.github.igorsergei4.sagademo.execution.service.entity;

import com.github.igorsergei4.sagademo.common.service.EntityWithIdService;
import com.github.igorsergei4.sagademo.execution.model.Client;
import com.github.igorsergei4.sagademo.execution.model.Execution;
import com.github.igorsergei4.sagademo.execution.model.Executor;
import com.github.igorsergei4.sagademo.execution.model.Offering;
import com.github.igorsergei4.sagademo.execution.repository.ExecutionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ExecutionService extends EntityWithIdService<Execution, ExecutionRepository> {
    public ExecutionService(ExecutionRepository repository) {
        super(repository);
    }

    public Optional<Execution> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    public Execution createExecution(
            Long orderId,
            Offering offering,
            Executor executor,
            Client client,
            LocalDate deadlineDate
    ) {
        Execution execution = new Execution();
        execution.setStatus(Execution.Status.AWAITING_PAYMENT);
        execution.setCreatedAt(LocalDateTime.now());

        execution.setOrderId(orderId);
        execution.setOffering(offering);
        execution.setCost(offering.getCost());
        execution.setExecutor(executor);
        execution.setClient(client);
        execution.setDeadlineOn(deadlineDate);

        return this.save(execution);
    }

    public Execution cancelExecution(Execution execution, Execution.Status cancelledStatus) {
        if (execution.getStatus().isAssignedWithExecutor()) {
            // TODO: implement rebuilding executor's execution plan
        }

        execution.setStatus(cancelledStatus);
        return this.save(execution);
    }
}
