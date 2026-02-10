package com.github.igorsergei4.sagademo.common.repository;

import com.github.igorsergei4.sagademo.common.model.VersionedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface VersionedEntityRepository <EntityT extends VersionedEntity<PrimaryKeyT>, PrimaryKeyT extends Serializable>
        extends JpaRepository<EntityT, PrimaryKeyT> {
}
