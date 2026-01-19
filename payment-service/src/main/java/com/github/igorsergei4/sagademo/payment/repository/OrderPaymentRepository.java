package com.github.igorsergei4.sagademo.payment.repository;

import com.github.igorsergei4.sagademo.common.repository.EntityWithIdRepository;
import com.github.igorsergei4.sagademo.payment.model.OrderPayment;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderPaymentRepository extends EntityWithIdRepository<OrderPayment> {
    Optional<OrderPayment> findByOrderId(Long orderId);
}
