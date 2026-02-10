package com.github.igorsergei4.sagademo.order.service.entity;

import com.github.igorsergei4.sagademo.common.exception.EntityNotFoundException;
import com.github.igorsergei4.sagademo.common.service.EntityWithIdService;
import com.github.igorsergei4.sagademo.order.dto.RegisterOrderParams;
import com.github.igorsergei4.sagademo.order.model.Offering;
import com.github.igorsergei4.sagademo.order.model.Order;
import com.github.igorsergei4.sagademo.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService extends EntityWithIdService<Order, OrderRepository> {
    private final ClientService clientService;
    private final OfferingService offeringService;

    public OrderService(
            OrderRepository repository,
            ClientService clientService,
            OfferingService offeringService
    ) {
        super(repository);
        this.clientService = clientService;
        this.offeringService = offeringService;
    }

    public Order createOrder(RegisterOrderParams registerOrderParams) throws EntityNotFoundException {
        Order order = new Order();

        Long clientId = registerOrderParams.getClientId();
        order.setClient(clientService.findById(clientId).orElseThrow(
                () -> new EntityNotFoundException("клиента", clientId)
        ));

        Long offeringId = registerOrderParams.getOfferingId();
        Offering offering = offeringService.findById(offeringId).orElseThrow(
                () -> new EntityNotFoundException("услугу", offeringId)
        );
        order.setOffering(offering);
        order.setCost(offering.getCost());

        order.setDeadlineOn(registerOrderParams.getDeadlineOn());
        order.setCreatedAt(LocalDateTime.now());

        order.setStatus(Order.Status.AWAITING_EXECUTOR);

        return this.save(order);
    }

    public IllegalStateException getOrderDisappearedException(Long orderId) {
        return new IllegalStateException("Order with id=" + orderId + " got disappeared!");
    }
}
