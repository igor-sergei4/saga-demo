package com.github.igorsergei4.sagademo.order.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class RegisterOrderParams {
    @NotNull
    private Long clientId;

    @NotNull
    private Long offeringId;

    @NotNull
    @Future
    private LocalDate deadlineOn;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getOfferingId() {
        return offeringId;
    }

    public void setOfferingId(Long offeringId) {
        this.offeringId = offeringId;
    }

    public LocalDate getDeadlineOn() {
        return deadlineOn;
    }

    public void setDeadlineOn(LocalDate deadlineOn) {
        this.deadlineOn = deadlineOn;
    }
}
