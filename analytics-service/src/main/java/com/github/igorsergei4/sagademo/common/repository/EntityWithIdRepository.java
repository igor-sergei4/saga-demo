package com.github.igorsergei4.sagademo.common.repository;

import com.github.igorsergei4.sagademo.common.model.EntityWithId;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityWithIdRepository<EntityT extends EntityWithId>
        extends VersionedEntityRepository<EntityT, Long> {
}
