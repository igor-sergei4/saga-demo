package com.github.igorsergei4.sagademo.execution.repository;

import com.github.igorsergei4.sagademo.common.repository.VersionedEntityRepository;
import com.github.igorsergei4.sagademo.execution.model.ExecutorStats;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutorStatsRepository extends VersionedEntityRepository<ExecutorStats, Long> {
}
