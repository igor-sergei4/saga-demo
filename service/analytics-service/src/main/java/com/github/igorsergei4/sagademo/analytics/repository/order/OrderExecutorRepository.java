package com.github.igorsergei4.sagademo.analytics.repository.order;

import com.github.igorsergei4.sagademo.analytics.model.order.OrderExecutor;
import com.github.igorsergei4.sagademo.analytics.repository.EntityWithIdProjectionRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderExecutorRepository extends EntityWithIdProjectionRepository<OrderExecutor> {
}
