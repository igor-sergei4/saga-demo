package com.github.igorsergei4.sagademo.execution.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "execution_item")
public class ExecutionItem {
    @EmbeddedId
    private ExecutionItemKey id;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionItem that = (ExecutionItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public ExecutionItemKey getId() {
        return id;
    }

    public void setId(ExecutionItemKey id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
