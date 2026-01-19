package com.github.igorsergei4.sagademo.order.model;

import com.github.igorsergei4.sagademo.common.model.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "client")
public class Client extends NamedEntity {
}
