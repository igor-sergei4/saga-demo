package com.github.igorsergei4.sagademo.analytics.repository.order;

import com.github.igorsergei4.sagademo.analytics.model.order.Order;
import com.github.igorsergei4.sagademo.analytics.repository.EntityWithIdProjectionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends OrderRepositoryAddOn, EntityWithIdProjectionRepository<Order> {
}
