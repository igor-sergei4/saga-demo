package com.github.igorsergei4.sagademo.common.dto;

import com.github.igorsergei4.sagademo.common.model.NamedEntity;

public class NamedEntityDto implements DtoWithId {
    private Long id;
    private String name;

    public NamedEntityDto(NamedEntity namedEntity) {
        this.id = namedEntity.getId();
        this.name = namedEntity.getName();
    }

    public NamedEntityDto() {}

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
