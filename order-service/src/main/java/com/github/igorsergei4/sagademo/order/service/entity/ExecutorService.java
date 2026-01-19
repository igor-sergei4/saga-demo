package com.github.igorsergei4.sagademo.order.service.entity;

import com.github.igorsergei4.sagademo.common.service.EntityWithIdService;
import com.github.igorsergei4.sagademo.order.model.Executor;
import com.github.igorsergei4.sagademo.order.repository.ExecutorRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ExecutorService extends EntityWithIdService<Executor, ExecutorRepository> {
    public ExecutorService(ExecutorRepository repository) {
        super(repository);
    }

    public Optional<Executor> findByRemoteId(Long remoteId) {
        return repository.findByRemoteId(remoteId);
    }
}
