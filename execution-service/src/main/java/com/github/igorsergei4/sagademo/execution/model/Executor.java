package com.github.igorsergei4.sagademo.execution.model;

import com.github.igorsergei4.sagademo.common.model.NamedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "executor")
public class Executor extends NamedEntity {

    @Column(name = "final_date", nullable = true)
    private LocalDate finalDate;

    @OneToOne(mappedBy = "executor", fetch = FetchType.LAZY, optional = false)
    private ExecutorStats executorStats;

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

    public ExecutorStats getExecutorStats() {
        return executorStats;
    }

    public void setExecutorStats(ExecutorStats executorStats) {
        this.executorStats = executorStats;
    }
}
