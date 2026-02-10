package com.github.igorsergei4.sagademo.execution.model;

import com.github.igorsergei4.sagademo.common.model.VersionedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * This entity is used to cause OptimisticLockException when either active_executions_count or daily_points
 * value is changed by another transaction while rebuilding executor's execution plan
 */

@Entity
@Table(name = "executor_stats")
public class ExecutorStats extends VersionedEntity<Long> {
    @Id
    @Column(name = "id_executor", nullable = false, unique = true)
    private Long executorId;

    @Column(name = "daily_points", nullable = false)
    private Integer dailyPoints;

    @Column(name = "active_executions_count", nullable = false)
    private int activeExecutionsCount;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_executor")
    @MapsId
    private Executor executor;

    @Override
    public Long getId() {
        return getExecutorId();
    }

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    public Integer getDailyPoints() {
        return dailyPoints;
    }

    public void setDailyPoints(Integer dailyPoints) {
        this.dailyPoints = dailyPoints;
    }

    public int getActiveExecutionsCount() {
        return activeExecutionsCount;
    }

    public void setActiveExecutionsCount(int activeExecutionsCount) {
        this.activeExecutionsCount = activeExecutionsCount;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
