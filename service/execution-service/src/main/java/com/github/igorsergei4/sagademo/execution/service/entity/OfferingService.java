package com.github.igorsergei4.sagademo.execution.service.entity;

import com.github.igorsergei4.sagademo.common.service.EntityWithIdService;
import com.github.igorsergei4.sagademo.execution.model.Offering;
import com.github.igorsergei4.sagademo.execution.repository.OfferingRepository;
import org.springframework.stereotype.Service;

@Service
public class OfferingService extends EntityWithIdService<Offering, OfferingRepository> {
    public OfferingService(OfferingRepository repository) {
        super(repository);
    }
}
