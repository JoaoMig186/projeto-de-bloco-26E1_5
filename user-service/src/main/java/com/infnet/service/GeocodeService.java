package com.infnet.service;

import com.infnet.kafka.KafkaService;
import com.infnet.repository.CustomerProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GeocodeService {

    private KafkaService kafkaService;

    public void getCustomerGeocode(Long id, String address) {
        kafkaService.sendCustomerCreatedEvent(id,address);
    }
}
