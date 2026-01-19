package com.github.igorsergei4.sagademo.order.controller;

import com.github.igorsergei4.sagademo.common.exception.EntityNotFoundException;
import com.github.igorsergei4.sagademo.order.dto.RegisterOrderParams;
import com.github.igorsergei4.sagademo.order.service.saga.CreateOrderSagaOrchestrator;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final CreateOrderSagaOrchestrator createOrderSagaOrchestrator;

    public OrderController(CreateOrderSagaOrchestrator createOrderSagaOrchestrator) {
        this.createOrderSagaOrchestrator = createOrderSagaOrchestrator;
    }

    @PostMapping
    public void createOrder(@RequestBody @Valid RegisterOrderParams registerOrderParams) throws EntityNotFoundException {
        createOrderSagaOrchestrator.registerOrder(registerOrderParams);
    }
}
