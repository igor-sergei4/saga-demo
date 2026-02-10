package com.github.igorsergei4.sagademo.execution.repository;

import com.github.igorsergei4.sagademo.execution.model.Execution;
import com.github.igorsergei4.sagademo.execution.model.ExecutionItem;
import com.github.igorsergei4.sagademo.execution.model.ExecutionItemKey;
import com.github.igorsergei4.sagademo.execution.model.Executor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExecutionItemRepository extends JpaRepository<ExecutionItem, ExecutionItemKey> {
    @Query("""
            select execItem
            from ExecutionItem execItem
                join fetch execItem.id.execution exectn
            where exectn.executor = :executor
                and execItem.id.date <= :lastDate
            """)
    List<ExecutionItem> findAllByExecutorAndLastDate(Executor executor, LocalDate lastDate);

    List<ExecutionItem> findAllByIdExecution(Execution execution);
}
