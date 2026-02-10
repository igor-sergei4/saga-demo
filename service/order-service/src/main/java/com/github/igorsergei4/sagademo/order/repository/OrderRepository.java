package com.github.igorsergei4.sagademo.order.repository;

import com.github.igorsergei4.sagademo.common.repository.EntityWithIdRepository;
import com.github.igorsergei4.sagademo.order.model.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends EntityWithIdRepository<Order> {
}
