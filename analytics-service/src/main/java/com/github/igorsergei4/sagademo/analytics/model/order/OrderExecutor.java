package com.github.igorsergei4.sagademo.analytics.model.order;

import com.github.igorsergei4.sagademo.analytics.model.NamedEntityProjection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_executor")
public class OrderExecutor extends NamedEntityProjection {
}
