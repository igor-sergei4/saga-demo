package com.github.igorsergei4.sagademo.analytics.service.entity;

import com.github.igorsergei4.sagademo.analytics.dto.OrderInfoDto;
import com.github.igorsergei4.sagademo.analytics.dto.ResponsePage;
import com.github.igorsergei4.sagademo.analytics.model.order.Order;
import com.github.igorsergei4.sagademo.analytics.model.order.OrderClient;
import com.github.igorsergei4.sagademo.analytics.model.order.OrderExecutor;
import com.github.igorsergei4.sagademo.analytics.model.order.OrderOffering;
import com.github.igorsergei4.sagademo.analytics.repository.order.OrderClientRepository;
import com.github.igorsergei4.sagademo.analytics.repository.order.OrderExecutorRepository;
import com.github.igorsergei4.sagademo.analytics.repository.order.OrderOfferingRepository;
import com.github.igorsergei4.sagademo.analytics.repository.order.OrderRepository;
import com.github.igorsergei4.sagademo.order.event.OrderInfoEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService extends EntityWithIdProjectionService {
    private final OrderRepository orderRepository;
    private final OrderClientRepository orderClientRepository;
    private final OrderOfferingRepository orderOfferingRepository;
    private final OrderExecutorRepository orderExecutorRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderClientRepository orderClientRepository,
            OrderOfferingRepository orderOfferingRepository, OrderExecutorRepository orderExecutorRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderClientRepository = orderClientRepository;
        this.orderOfferingRepository = orderOfferingRepository;
        this.orderExecutorRepository = orderExecutorRepository;
    }

    public void saveOrderInfo(OrderInfoEvent orderInfoEvent) {
        saveInfo(orderRepository, orderInfoEvent, this::mapOrderProperties, Order::new);
    }

    public Order getProxy(Long id) {
        return orderRepository.getReferenceById(id);
    }

    public ResponsePage<OrderInfoDto> getOrdersInfo(
            Optional<LocalDateTime> intervalStart,
            Optional<LocalDateTime> intervalEnd,
            Optional<String> orderStatus,
            Optional<String> executionStatus,
            Optional<String> paymentStatus,
            Integer page,
            int pageSize
    ) {
        ResponsePage<Order> ordersInfo = orderRepository.getOrders(
                intervalStart,
                intervalEnd,
                orderStatus,
                executionStatus,
                paymentStatus,
                page,
                pageSize
        );

        return new ResponsePage<>(
                ordersInfo.data().stream().map(OrderInfoDto::new).toList(),
                ordersInfo.totalItems()
        );
    }

    private void mapOrderProperties(Order order, OrderInfoEvent orderInfoEvent) {
        order.setClient(saveInfo(orderClientRepository, orderInfoEvent.client(), this::mapNamedEntityProperties, OrderClient::new));
        order.setOffering(saveInfo(orderOfferingRepository, orderInfoEvent.offering(), this::mapOfferingProperties, OrderOffering::new));
        order.setCost(orderInfoEvent.cost());
        order.setExecutor(saveInfo(orderExecutorRepository, orderInfoEvent.executor(), this::mapNamedEntityProperties, OrderExecutor::new));
        order.setCreatedAt(orderInfoEvent.createdAt());
        order.setDeadlineOn(orderInfoEvent.deadlineOn());
        order.setStatus(orderInfoEvent.status());
    }

    private void mapOfferingProperties(OrderOffering orderOffering, OrderInfoEvent.OfferingDto offeringDto) {
        mapNamedEntityProperties(orderOffering, offeringDto);
        orderOffering.setCost(offeringDto.getCost());
        orderOffering.setDeprecated(offeringDto.getIsDeprecated());
    }
}
