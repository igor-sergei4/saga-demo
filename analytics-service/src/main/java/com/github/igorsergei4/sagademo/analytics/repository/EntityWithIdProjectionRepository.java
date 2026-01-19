package com.github.igorsergei4.sagademo.analytics.repository;

import com.github.igorsergei4.sagademo.analytics.model.EntityWithIdProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityWithIdProjectionRepository<EntityT extends EntityWithIdProjection>
        extends JpaRepository<EntityT, Long> {
}
