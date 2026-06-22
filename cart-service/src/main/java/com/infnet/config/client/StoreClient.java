package com.infnet.config.client;

import com.infnet.DTO.AddItemDTO;
import com.infnet.DTO.ProductOrderInfoDTO;
import com.infnet.config.exception.BusinessException;
import com.infnet.domain.Cart;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "store-service")
public interface StoreClient {

    @Retry(name = "cartService")
    @CircuitBreaker(
            name = "cartService",
            fallbackMethod = "addItemFallback"
    )
    @GetMapping("/stores/products/{id}/order-info")
    ProductOrderInfoDTO getOrderInfo(@PathVariable("id") Long id);


    default ProductOrderInfoDTO addItemFallback(Long id, Throwable exception) {

        throw new BusinessException("A comunicação com o serviço de lojas falhou ou está indisponível. Não foi possível validar o produto no momento. Tente novamente mais tarde.");
    }
}
