package com.infnet.api.exception;

public class ForbiddenAuthorizationException extends RuntimeException {
    public ForbiddenAuthorizationException(String message) {
        super(message);
    }
}
