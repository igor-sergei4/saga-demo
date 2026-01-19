package com.github.igorsergei4.sagademo.order.repository;

import com.github.igorsergei4.sagademo.common.repository.EntityWithIdRepository;
import com.github.igorsergei4.sagademo.order.model.Executor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExecutorRepository extends EntityWithIdRepository<Executor> {
    Optional<Executor> findByRemoteId(Long remoteId);
}
