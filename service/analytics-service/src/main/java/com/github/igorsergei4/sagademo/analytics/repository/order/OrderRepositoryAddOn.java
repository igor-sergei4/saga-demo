package com.github.igorsergei4.sagademo.analytics.repository.order;

import com.github.igorsergei4.sagademo.analytics.dto.ResponsePage;
import com.github.igorsergei4.sagademo.analytics.model.order.Order;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepositoryAddOn {
    ResponsePage<Order> getOrders(
            Optional<LocalDateTime> intervalStart,
            Optional<LocalDateTime> intervalEnd,
            Optional<String> orderStatus,
            Optional<String> executionStatus,
            Optional<String> paymentStatus,
            Integer page,
            int pageSize
    );
}
