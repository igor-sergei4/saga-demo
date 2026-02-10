package com.github.igorsergei4.sagademo.analytics.dto;

import com.github.igorsergei4.sagademo.analytics.model.execution.Execution;
import com.github.igorsergei4.sagademo.analytics.model.execution.ExecutionClient;
import com.github.igorsergei4.sagademo.analytics.model.execution.ExecutionExecutor;
import com.github.igorsergei4.sagademo.analytics.model.execution.ExecutionOffering;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExecutionInfoDto(
        Long id,
        ClientDto client,
        OfferingDto offering,
        BigDecimal cost,
        ExecutorDto executor,
        LocalDateTime createdAt,
        LocalDate deadlineOn,
        String status
) {
    public ExecutionInfoDto(Execution execution) {
        this(
                execution.getId(),
                execution.getClient() == null ? null : new ClientDto(execution.getClient()),
                execution.getOffering() == null ? null : new OfferingDto(execution.getOffering()),
                execution.getCost(),
                execution.getExecutor() == null ? null : new ExecutorDto(execution.getExecutor()),
                execution.getCreatedAt(),
                execution.getDeadlineOn(),
                execution.getStatus()
        );
    }

    public record ClientDto(Long id, String name) {
        public ClientDto(ExecutionClient executionClient) {
            this(executionClient.getId(), executionClient.getName());
        }
    }

    public record OfferingDto(
            Long id,
            String name,
            BigDecimal cost,
            Integer points,
            LocalDateTime deprecatedAt
    ) {
        public OfferingDto(ExecutionOffering executionOffering) {
            this(
                    executionOffering.getId(),
                    executionOffering.getName(),
                    executionOffering.getCost(),
                    executionOffering.getPoints(),
                    executionOffering.getDeprecatedAt()
            );
        }
    }

    public record ExecutorDto(
            Long id,
            String name,
            Integer dailyPoints,
            LocalDate finalDate
    ) {
        public ExecutorDto(ExecutionExecutor executionExecutor) {
            this(
                    executionExecutor.getId(),
                    executionExecutor.getName(),
                    executionExecutor.getDailyPoints(),
                    executionExecutor.getFinalDate()
            );
        }
    }
}
