package com.github.igorsergei4.sagademo.payment.service.entity;

import com.github.igorsergei4.sagademo.common.service.EntityWithIdService;
import com.github.igorsergei4.sagademo.payment.model.Client;
import com.github.igorsergei4.sagademo.payment.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService extends EntityWithIdService<Client, ClientRepository> {
    public ClientService(ClientRepository repository) {
        super(repository);
    }

    public Optional<Client> findByRemoteId(Long remoteId) {
        return repository.findByRemoteId(remoteId);
    }
}
