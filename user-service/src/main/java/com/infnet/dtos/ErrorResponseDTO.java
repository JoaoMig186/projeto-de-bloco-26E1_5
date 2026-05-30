package com.infnet.dtos;


import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        List<ErrorFieldDTO> fieldErrors
) {
}
