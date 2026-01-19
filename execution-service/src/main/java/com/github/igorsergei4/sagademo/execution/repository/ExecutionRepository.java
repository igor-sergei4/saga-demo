package com.github.igorsergei4.sagademo.execution.repository;

import com.github.igorsergei4.sagademo.common.repository.EntityWithIdRepository;
import com.github.igorsergei4.sagademo.execution.model.Execution;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExecutionRepository extends EntityWithIdRepository<Execution> {
    Optional<Execution> findByOrderId(Long orderId);
}
