package com.infnet.config.client;

import com.infnet.DTO.ProductOrderInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "store-service")
public interface StoreClient {

    @GetMapping("/stores/products/{id}/order-info")
    ProductOrderInfoDTO getOrderInfo(@PathVariable("id") Long id);

}