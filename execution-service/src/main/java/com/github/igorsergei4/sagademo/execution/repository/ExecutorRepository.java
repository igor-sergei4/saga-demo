package com.github.igorsergei4.sagademo.execution.repository;

import com.github.igorsergei4.sagademo.common.repository.EntityWithIdRepository;
import com.github.igorsergei4.sagademo.execution.model.Executor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutorRepository extends EntityWithIdRepository<Executor> {
}
