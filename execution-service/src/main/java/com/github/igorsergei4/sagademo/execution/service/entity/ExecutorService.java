package com.github.igorsergei4.sagademo.execution.service.entity;

import com.github.igorsergei4.sagademo.common.service.EntityWithIdService;
import com.github.igorsergei4.sagademo.execution.model.Executor;
import com.github.igorsergei4.sagademo.execution.model.Offering;
import com.github.igorsergei4.sagademo.execution.repository.ExecutorRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ExecutorService extends EntityWithIdService<Executor, ExecutorRepository> {
    public ExecutorService(ExecutorRepository repository) {
        super(repository);
    }

    public Optional<Executor> findAppropriate(Offering offering, LocalDate deadlineOn) {
        return repository.findAll(PageRequest.ofSize(1)).stream().findAny();//TODO: implement actual logics
    }
}
