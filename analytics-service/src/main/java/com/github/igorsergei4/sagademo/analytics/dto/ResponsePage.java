package com.github.igorsergei4.sagademo.analytics.dto;

import java.util.List;

public record ResponsePage <RecordTypeT> (
        List<RecordTypeT> data,
        Number totalItems
) {
}
