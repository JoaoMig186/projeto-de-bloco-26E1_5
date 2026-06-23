package com.infnet.dto;

import com.infnet.domain.enums.PaymentMethod;

public record OrderRequestDTO(
        Long idStore,
        PaymentMethod paymentMethod) {

}
