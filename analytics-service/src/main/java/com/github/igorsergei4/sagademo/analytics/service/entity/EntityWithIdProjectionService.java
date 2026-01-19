package com.github.igorsergei4.sagademo.analytics.service.entity;

import com.github.igorsergei4.sagademo.analytics.model.EntityWithIdProjection;
import com.github.igorsergei4.sagademo.analytics.model.NamedEntityProjection;
import com.github.igorsergei4.sagademo.analytics.repository.EntityWithIdProjectionRepository;
import com.github.igorsergei4.sagademo.common.dto.DtoWithId;
import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

abstract class EntityWithIdProjectionService {
    protected
    <
            EntityT extends EntityWithIdProjection,
            RepositoryT extends EntityWithIdProjectionRepository<EntityT>,
            DtoT extends DtoWithId
    >
    EntityT saveInfo(
            RepositoryT repository,
            DtoT entityDto,
            BiConsumer<EntityT, DtoT> propertyMapper,
            Supplier<EntityT> newEntitySupplier
    ) {
        if (entityDto == null) {
            return null;
        }

        Long entityId = entityDto.getId();
        EntityT entity = repository.findById(entityId).orElseGet(() -> {
            EntityT newEntity = newEntitySupplier.get();
            newEntity.setId(entityId);
            return newEntity;
        });

        propertyMapper.accept(entity, entityDto);

        return repository.save(entity);
    }

    protected <EntityT extends NamedEntityProjection> void mapNamedEntityProperties(
            EntityT namedEntity,
            NamedEntityDto namedEntityDto
    ) {
        namedEntity.setName(namedEntityDto.getName());
    };
}
