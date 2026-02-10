package com.github.igorsergei4.sagademo.order.model;

import com.github.igorsergei4.sagademo.common.model.NamedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "executor")
public class Executor extends NamedEntity {
    @Column(name = "remote_id", nullable = true, unique = true)
    private Long remoteId;

    public Long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }
}
