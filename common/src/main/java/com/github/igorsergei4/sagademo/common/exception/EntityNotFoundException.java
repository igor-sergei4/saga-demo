package com.github.igorsergei4.sagademo.common.exception;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityName, Long entityId) {
        super(String.format("Не удалось найти %s с id=%d.", entityName, entityId));
    }
}
