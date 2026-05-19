package com.infnet.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("INVALID Email or Password");
    }
}
