package com.github.igorsergei4.sagademo.payment.repository;

import com.github.igorsergei4.sagademo.common.repository.EntityWithIdRepository;
import com.github.igorsergei4.sagademo.payment.model.Client;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends EntityWithIdRepository<Client> {
    Optional<Client> findByRemoteId(Long remoteId);
}
