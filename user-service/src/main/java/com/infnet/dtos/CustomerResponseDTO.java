package com.infnet.dtos;

import com.infnet.model.CustomerProfile;
import com.infnet.model.enums.Status;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public record CustomerResponseDTO(
        Long id,
        String address,
        Double lat,
        Double lon,
        Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CustomerResponseDTO toCustomerResponseDTO(CustomerProfile customer){
        return new CustomerResponseDTO(
                customer.getUserId(),
                customer.getAddress(),
                customer.getLat(),
                customer.getLon(),
                customer.getStatus(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }

}
