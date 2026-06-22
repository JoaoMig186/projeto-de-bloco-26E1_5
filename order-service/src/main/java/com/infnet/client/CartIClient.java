package com.infnet.client;

import com.infnet.dto.cart.ItemCarrinhoResponseDTO;
import com.infnet.dto.cart.PagamentoIniciadoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${api.endpoints.cart}")
public interface CartIClient {

    @GetMapping("/carts/order")
    PagamentoIniciadoResponseDTO getCart(@RequestHeader("X-User-Id") Long usuarioId);
}