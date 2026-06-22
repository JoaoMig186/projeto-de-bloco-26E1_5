package com.infnet.dto;

import com.infnet.domain.enums.PaymentMethod;

public record OrderRequestDTO(

        Long idUser,
        Long idStore,
        Long idCart,
        PaymentMethod paymentMethod) {

}
