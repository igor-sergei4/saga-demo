package com.github.igorsergei4.sagademo.execution.model;

import com.github.igorsergei4.sagademo.common.model.NamedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "executor")
public class Executor extends NamedEntity {
    @Column(name = "daily_points", nullable = false)
    private Integer dailyPoints;

    @Column(name = "final_date", nullable = true)
    private LocalDate finalDate;

    public Integer getDailyPoints() {
        return dailyPoints;
    }

    public void setDailyPoints(Integer dailyPoints) {
        this.dailyPoints = dailyPoints;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }
}
