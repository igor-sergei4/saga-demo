package com.github.igorsergei4.sagademo.analytics.repository.execution;

import com.github.igorsergei4.sagademo.analytics.model.execution.ExecutionClient;
import com.github.igorsergei4.sagademo.analytics.repository.EntityWithIdProjectionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionClientRepository extends EntityWithIdProjectionRepository<ExecutionClient> {
}
