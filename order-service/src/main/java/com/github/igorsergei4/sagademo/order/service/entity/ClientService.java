package com.github.igorsergei4.sagademo.order.service.entity;

import com.github.igorsergei4.sagademo.common.service.EntityWithIdService;
import com.github.igorsergei4.sagademo.order.model.Client;
import com.github.igorsergei4.sagademo.order.repository.ClientRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ClientService extends EntityWithIdService<Client, ClientRepository> {
    public ClientService(ClientRepository repository) {
        super(repository);
    }
}
