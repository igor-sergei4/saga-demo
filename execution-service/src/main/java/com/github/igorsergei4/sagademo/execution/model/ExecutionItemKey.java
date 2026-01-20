package com.github.igorsergei4.sagademo.execution.model;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.util.Objects;

public class ExecutionItemKey {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_execution", nullable = false)
    private Execution execution;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    public ExecutionItemKey(Execution execution, LocalDate date) {
        this.execution = execution;
        this.date = date;
    }

    public ExecutionItemKey() {}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionItemKey that = (ExecutionItemKey) o;
        return Objects.equals(execution, that.execution) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(execution, date);
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
