package com.infnet.service.cart;

import com.infnet.client.CartIClient;
import com.infnet.controller.GlobalExceptionHandler;
import com.infnet.dto.cart.PagamentoIniciadoResponseDTO;
import com.infnet.excepton.CartServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile({"prod"})
public class CartServiceImpl implements CartService {
    private final CartIClient cartIClient;

    @Override
    @CircuitBreaker(name = "cartService", fallbackMethod = "fallBackMethod")
    @Retry(name = "cartService")
    public PagamentoIniciadoResponseDTO getCart(Long usuarioId) {
        return cartIClient.getCart(usuarioId);
    }
    public PagamentoIniciadoResponseDTO fallBackMethod(Long id, Throwable throwable){
        throw new CartServiceUnavailableException("Sistema de Carrinho fora do Ar, tente novamente em instantes!");
    }
}
