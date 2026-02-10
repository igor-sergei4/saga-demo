package com.github.igorsergei4.sagademo.common.service;

import com.github.igorsergei4.sagademo.common.model.EntityWithId;
import com.github.igorsergei4.sagademo.common.repository.EntityWithIdRepository;

public abstract class EntityWithIdService<
        EntityT extends EntityWithId,
        RepositoryT extends EntityWithIdRepository<EntityT>
> extends VersionedEntityService<EntityT, Long, RepositoryT> {
    public EntityWithIdService(RepositoryT repository) {
        super(repository);
    }
}
