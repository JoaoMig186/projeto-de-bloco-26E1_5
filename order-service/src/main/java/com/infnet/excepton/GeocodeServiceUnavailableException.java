package com.infnet.excepton;

public class GeocodeServiceUnavailableException extends RuntimeException {
    public GeocodeServiceUnavailableException(String message) {
        super(message);
    }
}
