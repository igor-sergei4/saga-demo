package com.github.igorsergei4.sagademo.payment.model;

import com.github.igorsergei4.sagademo.common.model.NamedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "client")
public class Client extends NamedEntity {
    @Column(name = "remote_id", nullable = true, unique = true)
    private Long remoteId;

    public Long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }
}
