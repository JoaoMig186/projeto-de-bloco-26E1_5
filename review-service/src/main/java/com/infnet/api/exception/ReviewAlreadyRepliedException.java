package com.infnet.api.exception;

public class ReviewAlreadyRepliedException extends RuntimeException {
    public ReviewAlreadyRepliedException(String message) {
        super(message);
    }
}
