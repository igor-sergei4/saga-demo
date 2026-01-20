package com.github.igorsergei4.sagademo.execution.service.entity;

import com.github.igorsergei4.sagademo.common.model.EntityWithId;
import com.github.igorsergei4.sagademo.common.service.EntityWithIdService;
import com.github.igorsergei4.sagademo.execution.model.Client;
import com.github.igorsergei4.sagademo.execution.model.Execution;
import com.github.igorsergei4.sagademo.execution.model.ExecutionItem;
import com.github.igorsergei4.sagademo.execution.model.ExecutionItemKey;
import com.github.igorsergei4.sagademo.execution.model.Executor;
import com.github.igorsergei4.sagademo.execution.model.ExecutorStats;
import com.github.igorsergei4.sagademo.execution.model.Offering;
import com.github.igorsergei4.sagademo.execution.repository.ExecutionItemRepository;
import com.github.igorsergei4.sagademo.execution.repository.ExecutionRepository;
import com.github.igorsergei4.sagademo.execution.repository.ExecutorStatsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExecutionService extends EntityWithIdService<Execution, ExecutionRepository> {
    private final ExecutorStatsRepository executorStatsRepository;
    private final ExecutionItemRepository executionItemRepository;

    public ExecutionService(
            ExecutionRepository repository,
            ExecutorStatsRepository executorStatsRepository,
            ExecutionItemRepository executionItemRepository
    ) {
        super(repository);
        this.executorStatsRepository = executorStatsRepository;
        this.executionItemRepository = executionItemRepository;
    }

    public Optional<Execution> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    public Execution createExecution(
            Long orderId,
            Offering offering,
            Executor executor,
            Client client,
            LocalDate deadlineDate
    ) {
        Execution execution = new Execution();
        execution.setStatus(Execution.Status.AWAITING_PAYMENT);
        execution.setCreatedAt(LocalDateTime.now());

        execution.setOrderId(orderId);
        execution.setOffering(offering);
        execution.setCost(offering.getCost());
        execution.setExecutor(executor);
        execution.setClient(client);
        execution.setDeadlineOn(deadlineDate);

        execution = this.save(execution);
        assignExecution(execution, executor);

        return execution;
    }

    public Execution cancelExecution(Execution execution, Execution.Status cancelledStatus) {
        if (execution.getStatus().isAssignedWithExecutor()) {
            unassignExecution(execution);
        }

        execution.setStatus(cancelledStatus);
        return this.save(execution);
    }

    private void assignExecution(Execution execution, Executor executor) {
        ExecutorStats executorStats = executor.getExecutorStats();

        addAnotherExecutionToCurrentExecutionPlan(executor, execution);

        executorStats.setActiveExecutionsCount(executorStats.getActiveExecutionsCount() + 1);
        executorStatsRepository.save(executorStats);
    }

    private void unassignExecution(Execution execution) {
        Executor executor = execution.getExecutor();
        ExecutorStats executorStats = executor.getExecutorStats();

        List<ExecutionItem> executionsToDelete = executionItemRepository.findAllByIdExecution(execution);
        Optional<LocalDate> upperBoundOfIntervalToRebuildOptional = executionsToDelete.stream()
                .map(executionItem -> executionItem.getId().getDate())
                .max(Comparator.naturalOrder());
        executionItemRepository.deleteAll(executionsToDelete);

        upperBoundOfIntervalToRebuildOptional.ifPresent(upperBoundOfIntervalToRebuild -> {
            List<ExecutionItem> oldExecutionItems = executionItemRepository.findAllByExecutorAndLastDate(
                    executor,
                    execution.getDeadlineOn()
            );

            rebuildExecutionPlan(
                    oldExecutionItems,
                    getExecutionByPointsToCover(oldExecutionItems),
                    upperBoundOfIntervalToRebuild,
                    executorStats.getDailyPoints()
            );
        });

        executorStats.setActiveExecutionsCount(executorStats.getActiveExecutionsCount() - 1);
        executorStatsRepository.save(executorStats);
    }

    private void addAnotherExecutionToCurrentExecutionPlan(Executor executor, Execution execution) {
        List<ExecutionItem> oldExecutionItems = executionItemRepository.findAllByExecutorAndLastDate(
                executor,
                execution.getDeadlineOn()
        );

        LocalDate currentlyProcessedDate = execution.getDeadlineOn();
        LocalDate maxDateInInterval = oldExecutionItems.stream()
                .map(executionItem -> executionItem.getId().getDate())
                .max(Comparator.naturalOrder())
                .orElse(LocalDate.MIN);
        Integer pointsToCover = execution.getOffering().getPoints();
        Integer dailyPoints = executor.getExecutorStats().getDailyPoints();

        List<ExecutionItem> newExecutionItems = new ArrayList<>();
        while (maxDateInInterval.isBefore(currentlyProcessedDate) && pointsToCover > 0) {
            ExecutionItem newExecutionItem = getNewExecutionItem(execution, currentlyProcessedDate, dailyPoints, pointsToCover);
            newExecutionItems.add(newExecutionItem);
            pointsToCover -= newExecutionItem.getPoints();
            currentlyProcessedDate = currentlyProcessedDate.minusDays(1);
        }

        executionItemRepository.saveAll(newExecutionItems);
        if (pointsToCover > 0) {
            addNewItemsToExistingInterval(oldExecutionItems, execution, currentlyProcessedDate, pointsToCover, dailyPoints);
        }
    }

    private void addNewItemsToExistingInterval(
            List<ExecutionItem> oldExecutionItems,
            Execution newExecution,
            LocalDate currentlyProcessedDate,
            Integer newExecutionPointsToCover,
            Integer dailyPoints
    ) {
        Map<Execution, Integer> executionByPointsToCover = getExecutionByPointsToCover(oldExecutionItems);
        executionByPointsToCover.put(newExecution, newExecutionPointsToCover);

        rebuildExecutionPlan(
                oldExecutionItems,
                executionByPointsToCover,
                currentlyProcessedDate,
                dailyPoints
        );
    }

    private void rebuildExecutionPlan(
            List<ExecutionItem> oldExecutionItems,
            Map<Execution, Integer> executionByPointsToCover,
            LocalDate currentlyProcessedDate,
            Integer dailyPoints
    ) {
        List<Execution> sortedExecutions = executionByPointsToCover.keySet()
                .stream()
                .sorted(getExecutionPriorityComparator(executionByPointsToCover))
                .toList();
        List<ExecutionItem> newExecutionItems = new ArrayList<>();

        Integer dateRemainingPoints = dailyPoints;
        for (Execution execution : sortedExecutions) {
            Integer pointsToCover = executionByPointsToCover.get(execution);

            if (execution.getDeadlineOn().isBefore(currentlyProcessedDate)) {
                currentlyProcessedDate = execution.getDeadlineOn();
                dateRemainingPoints = dailyPoints;
            }

            while (pointsToCover > 0) {
                ExecutionItem newExecutionItem = getNewExecutionItem(
                        execution,
                        currentlyProcessedDate,
                        dateRemainingPoints,
                        pointsToCover
                );
                newExecutionItems.add(newExecutionItem);

                Integer coveredPoints = newExecutionItem.getPoints();
                dateRemainingPoints -= coveredPoints;
                if (dateRemainingPoints == 0) {
                    currentlyProcessedDate = currentlyProcessedDate.minusDays(1);
                    dateRemainingPoints = dailyPoints;
                }

                pointsToCover -= coveredPoints;
            }
        }

        executionItemRepository.deleteAll(oldExecutionItems);
        executionItemRepository.saveAll(newExecutionItems);
    }

    private Map<Execution, Integer> getExecutionByPointsToCover(List<ExecutionItem> executionItems) {
        return executionItems.stream().collect(Collectors.toMap(
                executionItem -> executionItem.getId().getExecution(),
                ExecutionItem::getPoints,
                Integer::sum
        ));
    }

    private Comparator<Execution> getExecutionPriorityComparator(Map<Execution, Integer> executionByPointsToCover) {
        return (
                Comparator.comparing(Execution::getDeadlineOn)
                        .thenComparing(executionByPointsToCover::get)
                        .thenComparing(EntityWithId::getId)
        ).reversed();
    }

    private ExecutionItem getNewExecutionItem(
            Execution execution,
            LocalDate date,
            Integer dateRemainingPoints,
            Integer pointsToCover
    ) {
        ExecutionItemKey executionItemKey = new ExecutionItemKey(execution, date);
        ExecutionItem executionItem = new ExecutionItem();
        executionItem.setId(executionItemKey);
        executionItem.setPoints(Math.min(dateRemainingPoints, pointsToCover));
        return executionItem;
    }
}
