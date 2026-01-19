package com.github.igorsergei4.sagademo.order.repository;

import com.github.igorsergei4.sagademo.common.repository.EntityWithIdRepository;
import com.github.igorsergei4.sagademo.order.model.Offering;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfferingRepository extends EntityWithIdRepository<Offering> {
    Optional<Offering> findByRemoteId(Long remoteId);
}
