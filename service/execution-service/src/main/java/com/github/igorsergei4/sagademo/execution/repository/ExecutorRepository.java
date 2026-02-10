package com.github.igorsergei4.sagademo.execution.repository;

import com.github.igorsergei4.sagademo.common.repository.EntityWithIdRepository;
import com.github.igorsergei4.sagademo.execution.model.Execution;
import com.github.igorsergei4.sagademo.execution.model.Executor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ExecutorRepository extends EntityWithIdRepository<Executor> {
    @Query("""
            select exec
            from Executor exec
                join fetch exec.executorStats execStats
                left join Execution exectn on exectn.executor = exec
                left join ExecutionItem execItem on execItem.id.execution = exectn
            where (exec.finalDate is null or exec.finalDate < :deadlineOn)
                and (execItem.id.date <= :deadlineOn or execItem is null)
            group by exec, execStats
            having (execStats.dailyPoints * :executionDays) - coalesce(sum(execItem.points), 0) >= :executionPoints
            order by exec.finalDate desc nulls first,
                coalesce(sum(execItem.points), 0) / execStats.dailyPoints
            limit 1
            """)
    Optional<Executor> findAppropriateForExecution(
            Integer executionPoints,
            LocalDate deadlineOn,
            long executionDays,
            Iterable<Execution.Status> reservingStatuses
    );
}
