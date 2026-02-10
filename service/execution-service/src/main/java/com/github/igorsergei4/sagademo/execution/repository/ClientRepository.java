package com.github.igorsergei4.sagademo.execution.repository;

import com.github.igorsergei4.sagademo.common.repository.EntityWithIdRepository;
import com.github.igorsergei4.sagademo.execution.model.Client;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends EntityWithIdRepository<Client> {
    Optional<Client> findByRemoteId(Long remoteId);
}
