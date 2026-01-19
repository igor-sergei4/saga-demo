package com.github.igorsergei4.sagademo.execution.service.entity;

import com.github.igorsergei4.sagademo.common.service.EntityWithIdService;
import com.github.igorsergei4.sagademo.execution.model.Client;
import com.github.igorsergei4.sagademo.execution.repository.ClientRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClientService extends EntityWithIdService<Client, ClientRepository> {
    public ClientService(ClientRepository repository) {
        super(repository);
    }

    public Optional<Client> findByRemoteId(Long remoteId) {
        return repository.findByRemoteId(remoteId);
    }
}
