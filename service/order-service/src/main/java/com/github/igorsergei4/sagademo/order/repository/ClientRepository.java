package com.github.igorsergei4.sagademo.order.repository;

import com.github.igorsergei4.sagademo.common.repository.EntityWithIdRepository;
import com.github.igorsergei4.sagademo.order.model.Client;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends EntityWithIdRepository<Client> {
}
