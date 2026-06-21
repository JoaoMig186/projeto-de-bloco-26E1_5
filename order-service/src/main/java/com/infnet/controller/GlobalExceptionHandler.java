package com.infnet.controller;

import com.infnet.excepton.CartServiceUnavailableException;
import com.infnet.excepton.DeliveryServiceUnavailableException;
import com.infnet.excepton.GeocodeServiceUnavailableException;
import com.infnet.excepton.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                "ORDER_NOT_FOUND",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(DeliveryServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleDeliveryUnavailable(DeliveryServiceUnavailableException ex) {
        ErrorResponse response = new ErrorResponse(
                "FRETE_INDISPONIVEL",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE) // 503
                .body(response);
    }

    @ExceptionHandler(GeocodeServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleGeocodeServiceUnavailable(GeocodeServiceUnavailableException ex) {
        ErrorResponse response = new ErrorResponse(
                "LOCALIZACAO_INDISPONIVEL",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }

    @ExceptionHandler(CartServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleCartServiceUnavailable(CartServiceUnavailableException ex) {
        ErrorResponse response = new ErrorResponse(
                "CART_INDISPONIVEL",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }

    public record ErrorResponse(
            String codigo,
            String mensagem
    ) {
    }
}
