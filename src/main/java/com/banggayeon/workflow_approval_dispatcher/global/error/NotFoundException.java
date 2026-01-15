package com.banggayeon.workflow_approval_dispatcher.global.error;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
