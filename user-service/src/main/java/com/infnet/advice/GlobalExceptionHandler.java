package com.infnet.advice;


import com.infnet.dtos.ErrorFieldDTO;
import com.infnet.dtos.ErrorResponseDTO;
import com.infnet.exception.AuthorizationException;
import com.infnet.exception.EmailAlreadyRegisteredException;
import com.infnet.exception.ForbiddenAuthorizationException;
import com.infnet.exception.UserInactiveException;
import com.infnet.kafka.KafkaCustomerListener;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthorizationException(AuthorizationException ex){
        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ForbiddenAuthorizationException.class)
    public ResponseEntity<ErrorResponseDTO> handleForbiddenAuthorizationException( ForbiddenAuthorizationException ex){
        ErrorResponseDTO error = new ErrorResponseDTO(
          LocalDateTime.now(),
          HttpStatus.FORBIDDEN.value(),
          "Forbidden",
          ex.getMessage(),
          List.of()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(UserInactiveException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserInactiveException( UserInactiveException ex){
        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }


    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredException ex){
        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<ErrorFieldDTO> fieldErrors = ex
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorFieldDTO(error.getField(),error.getDefaultMessage()))
                .toList();

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Validation Error",
                fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ErrorResponseDTO> handleRequestNotPermittedException(RequestNotPermitted ex){
        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Too Many Requests",
                ex.getMessage(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error);
    }

}
