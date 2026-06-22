package com.infnet.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${api.endpoints.payment}")
public interface PaymentIClient {
}
