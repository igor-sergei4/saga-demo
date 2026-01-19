package com.github.igorsergei4.sagademo.analytics.repository.execution;

import com.github.igorsergei4.sagademo.analytics.model.execution.ExecutionOffering;
import com.github.igorsergei4.sagademo.analytics.repository.EntityWithIdProjectionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionOfferingRepository extends EntityWithIdProjectionRepository<ExecutionOffering> {
}
