package com.infnet.excepton;

public class DeliveryServiceUnavailableException extends RuntimeException {
    public DeliveryServiceUnavailableException(String message) {
        super(message);
    }
}
