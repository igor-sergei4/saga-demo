package com.github.igorsergei4.sagademo.common.service;

import com.github.igorsergei4.sagademo.common.model.VersionedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

public class VersionedEntityService<
        EntityT extends VersionedEntity<PrimaryKeyT>,
        PrimaryKeyT extends Serializable,
        RepositoryT extends JpaRepository<EntityT, PrimaryKeyT>
> {
    protected final RepositoryT repository;

    public VersionedEntityService(RepositoryT repository) {
        this.repository = repository;
    }

    public Optional<EntityT> findById(PrimaryKeyT id) {
        return repository.findById(id);
    }

    @Transactional
    public EntityT save(EntityT entity) {
        return repository.save(entity);
    }

    @Transactional
    public void delete(EntityT entity) {
        repository.delete(entity);
    }

    public EntityT getProxy(PrimaryKeyT id) {
        return repository.getReferenceById(id);
    }
}
