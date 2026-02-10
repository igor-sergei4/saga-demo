package com.github.igorsergei4.sagademo.order.service.entity;

import com.github.igorsergei4.sagademo.common.service.EntityWithIdService;
import com.github.igorsergei4.sagademo.order.model.Offering;
import com.github.igorsergei4.sagademo.order.repository.OfferingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OfferingService extends EntityWithIdService<Offering, OfferingRepository> {
    public OfferingService(OfferingRepository repository) {
        super(repository);
    }

    public Optional<Offering> findByRemoteId(Long remoteId) {
        return repository.findByRemoteId(remoteId);
    }
}
