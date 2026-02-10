package com.github.igorsergei4.sagademo.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public abstract class VersionedEntity<PrimaryKeyT extends Serializable> {
    @Version
    @Column(name = "version")
    private int version;

    public abstract PrimaryKeyT getId();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VersionedEntity<?> versionedEntity = (VersionedEntity<?>) o;
        return Objects.equals(getId(), versionedEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
