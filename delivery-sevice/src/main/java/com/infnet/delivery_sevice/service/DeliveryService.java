package com.infnet.delivery_sevice.service;

import com.infnet.delivery_sevice.repository.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(
            DeliveryRepository deliveryRepository
    ){
        this.deliveryRepository = deliveryRepository;
    }
}
