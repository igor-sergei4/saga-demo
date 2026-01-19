package com.github.igorsergei4.sagademo.common.microservice.producer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ConditionalOnProperty(name = "microservice.is-producer", havingValue = "true")
public interface QueuedEventRepository extends JpaRepository<QueuedEvent, Long> {
    List<QueuedEvent> findByIsDeliveredFalseOrderByCreatedAtAsc(Pageable pageable);
}
