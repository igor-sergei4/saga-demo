package com.github.igorsergei4.sagademo.payment.service.entity;

import com.github.igorsergei4.sagademo.common.service.EntityWithIdService;
import com.github.igorsergei4.sagademo.payment.model.OrderPayment;
import com.github.igorsergei4.sagademo.payment.repository.OrderPaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderPaymentService extends EntityWithIdService<OrderPayment, OrderPaymentRepository> {
    public OrderPaymentService(OrderPaymentRepository repository) {
        super(repository);
    }

    public Optional<OrderPayment> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }
}
