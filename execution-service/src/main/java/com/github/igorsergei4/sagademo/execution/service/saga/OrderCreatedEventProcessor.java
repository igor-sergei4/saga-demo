package com.github.igorsergei4.sagademo.execution.service.saga;

import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;
import com.github.igorsergei4.sagademo.common.exception.EntityNotFoundException;
import com.github.igorsergei4.sagademo.common.microservice.consumer.EventProcessor;
import com.github.igorsergei4.sagademo.common.microservice.producer.QueuedEventService;
import com.github.igorsergei4.sagademo.execution.event.OrderExecutionRejectedEvent;
import com.github.igorsergei4.sagademo.execution.event.OrderExecutorAssignedEvent;
import com.github.igorsergei4.sagademo.execution.model.Client;
import com.github.igorsergei4.sagademo.execution.model.Execution;
import com.github.igorsergei4.sagademo.execution.model.Executor;
import com.github.igorsergei4.sagademo.execution.model.Offering;
import com.github.igorsergei4.sagademo.execution.service.analytics.AnalyticsServiceDataSender;
import com.github.igorsergei4.sagademo.execution.service.entity.ClientService;
import com.github.igorsergei4.sagademo.execution.service.entity.ExecutionService;
import com.github.igorsergei4.sagademo.execution.service.entity.ExecutorService;
import com.github.igorsergei4.sagademo.execution.service.entity.OfferingService;
import com.github.igorsergei4.sagademo.order.event.OrderCreatedEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class OrderCreatedEventProcessor implements EventProcessor<OrderCreatedEvent> {
    private static final String ORDER_EXECUTION_REJECTED_TOPIC = "order-execution-rejected";
    private static final String ORDER_EXECUTOR_ASSIGNED_TOPIC = "order-executor-assigned";

    private final ExecutionService executionService;
    private final OfferingService offeringService;
    private final ExecutorService executorService;
    private final ClientService clientService;

    private final AnalyticsServiceDataSender analyticsServiceDataSender;

    private final QueuedEventService queuedEventService;

    public OrderCreatedEventProcessor(
            ExecutionService executionService,
            OfferingService offeringService,
            ExecutorService executorService,
            ClientService clientService,
            AnalyticsServiceDataSender analyticsServiceDataSender,
            QueuedEventService queuedEventService
    ) {
        this.executionService = executionService;
        this.offeringService = offeringService;
        this.executorService = executorService;
        this.clientService = clientService;
        this.analyticsServiceDataSender = analyticsServiceDataSender;
        this.queuedEventService = queuedEventService;
    }

    @Override
    public void process(OrderCreatedEvent orderCreatedEvent) {
        Long orderId = orderCreatedEvent.orderId();
        if (executionService.findByOrderId(orderId).isPresent()) {
            throw new IllegalStateException("Order with id=" + orderId + " was already created.");
        }

        OrderCreatedEvent.OfferingDto remoteOfferingDto = orderCreatedEvent.offering();
        Long offeringId = remoteOfferingDto.getId();
        Offering offering;
        try {
            offering = getOfferingById(offeringId);
        } catch (EntityNotFoundException _ignored) {
            handleNoSuchOffering(orderId, offeringId);
            return;
        }

        if (remoteOfferingIsExpired(remoteOfferingDto, offering)) {
            handleRemoteOfferingBeingExpired(orderId, offering);
            return;
        }

        LocalDate deadlineDate = orderCreatedEvent.deadlineOn();
        Optional<Executor> executorOptional = executorService.findAppropriate(offering, deadlineDate);
        executorOptional.ifPresentOrElse(
                executor -> initOrderExecution(
                        orderId,
                        offering,
                        executor,
                        orderCreatedEvent.client(),
                        deadlineDate
                ),
                () -> handleNoAvailableExecutors(orderId)
        );
    }

    private Offering getOfferingById(Long offeringId) throws EntityNotFoundException {
        return offeringService.findById(offeringId).orElseThrow(() -> new EntityNotFoundException("No such offering."));
    }

    private void handleNoSuchOffering(Long orderId, Long offeringId) {
        OrderExecutionRejectedEvent rejectedForNoSuchOfferingEvent = new OrderExecutionRejectedEvent(
                orderId,
                OrderExecutionRejectedEvent.RejectionReason.NO_SUCH_OFFERING,
                new OrderExecutionRejectedEvent.OfferingDto(offeringId)
        );
        queuedEventService.queueAnEvent(
                ORDER_EXECUTION_REJECTED_TOPIC,
                orderId.toString(),
                rejectedForNoSuchOfferingEvent
        );
    }

    private boolean remoteOfferingIsExpired(OrderCreatedEvent.OfferingDto remoteOfferingDto, Offering offering) {
        return offering.isDeprecated()
                || !offering.getName().equals(remoteOfferingDto.getName())
                || offering.getCost().compareTo(remoteOfferingDto.getCost()) != 0;
    }

    private void handleRemoteOfferingBeingExpired(Long orderId, Offering offering) {
        OrderExecutionRejectedEvent rejectedForOfferingIsExpiredEvent = new OrderExecutionRejectedEvent(
                orderId,
                OrderExecutionRejectedEvent.RejectionReason.OFFERING_EXPIRED,
                new OrderExecutionRejectedEvent.OfferingDto(offering)
        );
        queuedEventService.queueAnEvent(
                ORDER_EXECUTION_REJECTED_TOPIC,
                orderId.toString(),
                rejectedForOfferingIsExpiredEvent
        );
    }

    private void handleNoAvailableExecutors(Long orderId) {
        OrderExecutionRejectedEvent rejectedForNoAvailableExecutorsEvent = new OrderExecutionRejectedEvent(
                orderId,
                OrderExecutionRejectedEvent.RejectionReason.NO_EXECUTORS,
                null
        );
        queuedEventService.queueAnEvent(
                ORDER_EXECUTION_REJECTED_TOPIC,
                orderId.toString(),
                rejectedForNoAvailableExecutorsEvent
        );
    }

    private void initOrderExecution(
            Long orderId,
            Offering offering,
            Executor executor,
            NamedEntityDto remoteClientDto,
            LocalDate deadlineDate
    ) {
        Execution execution = executionService.createExecution(
                orderId,
                offering,
                executor,
                getClient(remoteClientDto),
                deadlineDate
        );

        notifyAboutAssignedExecutor(execution);
        analyticsServiceDataSender.provideExecutionInfo(execution);
    }

    private Client getClient(NamedEntityDto remoteClientDto) {
        Long clientId = remoteClientDto.getId();
        String clientName = remoteClientDto.getName();
        return clientService.findByRemoteId(clientId)
                .map(localClient -> {
                    localClient.setName(clientName);
                    return localClient;
                })
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setRemoteId(clientId);
                    newClient.setName(clientName);
                    return clientService.save(newClient);
                });
    }

    private void notifyAboutAssignedExecutor(Execution execution) {
        Long orderId = execution.getOrderId();
        OrderExecutorAssignedEvent orderExecutorAssignedEvent = new OrderExecutorAssignedEvent(
                orderId,
                new NamedEntityDto(execution.getExecutor())
        );
        queuedEventService.queueAnEvent(
                ORDER_EXECUTOR_ASSIGNED_TOPIC,
                orderId.toString(),
                orderExecutorAssignedEvent
        );
    }
}
