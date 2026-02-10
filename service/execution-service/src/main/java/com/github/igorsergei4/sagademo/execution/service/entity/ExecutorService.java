package com.github.igorsergei4.sagademo.execution.service.entity;

import com.github.igorsergei4.sagademo.common.service.EntityWithIdService;
import com.github.igorsergei4.sagademo.execution.model.Execution;
import com.github.igorsergei4.sagademo.execution.model.Executor;
import com.github.igorsergei4.sagademo.execution.model.Offering;
import com.github.igorsergei4.sagademo.execution.repository.ExecutorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

@Service
public class ExecutorService extends EntityWithIdService<Executor, ExecutorRepository> {
    public ExecutorService(ExecutorRepository repository) {
        super(repository);
    }

    public Optional<Executor> findAppropriate(Offering offering, LocalDate deadlineOn) {
        // weekends are not considered since these logics are beyond the scope of the task
        long executionDays = ChronoUnit.DAYS.between(LocalDate.now(), deadlineOn);

        return repository.findAppropriateForExecution(
                offering.getPoints(),
                deadlineOn,
                executionDays,
                Arrays.stream(Execution.Status.values()).filter(Execution.Status::isReservingExecutionPoints).toList()
        );
    }
}
