package com.infnet.exception;

public class ForbiddenAuthorizationException extends RuntimeException {
    public ForbiddenAuthorizationException(String message) {
        super(message);
    }
}
