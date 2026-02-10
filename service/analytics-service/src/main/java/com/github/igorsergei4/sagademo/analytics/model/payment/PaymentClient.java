package com.github.igorsergei4.sagademo.analytics.model.payment;

import com.github.igorsergei4.sagademo.analytics.model.NamedEntityProjection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_client")
public class PaymentClient extends NamedEntityProjection {
}
