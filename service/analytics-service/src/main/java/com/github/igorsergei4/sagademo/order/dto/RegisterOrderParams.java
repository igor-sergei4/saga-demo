package com.github.igorsergei4.sagademo.order.dto;

public record RegisterOrderParams(
        String clientId,
        String offeringId,
        String deadlineOn
) {
}
