package com.github.igorsergei4.sagademo.analytics.repository.payment;

import com.github.igorsergei4.sagademo.analytics.model.payment.OrderPayment;
import com.github.igorsergei4.sagademo.analytics.repository.EntityWithIdProjectionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPaymentRepository extends EntityWithIdProjectionRepository<OrderPayment> {
}
