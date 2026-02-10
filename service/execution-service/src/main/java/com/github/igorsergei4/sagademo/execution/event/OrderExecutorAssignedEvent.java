package com.github.igorsergei4.sagademo.execution.event;

import com.github.igorsergei4.sagademo.common.dto.NamedEntityDto;

public record OrderExecutorAssignedEvent(
        Long orderId,
        NamedEntityDto executor
) {}
