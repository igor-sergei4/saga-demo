package com.github.igorsergei4.sagademo.analytics.model.execution;

import com.github.igorsergei4.sagademo.analytics.model.NamedEntityProjection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "execution_client")
public class ExecutionClient extends NamedEntityProjection {
}
