package com.github.igorsergei4.sagademo.analytics.model.execution;

import com.github.igorsergei4.sagademo.analytics.model.NamedEntityProjection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "execution_executor")
public class ExecutionExecutor extends NamedEntityProjection {
    @Column(name = "daily_points")
    private Integer dailyPoints;

    @Column(name = "final_date")
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
