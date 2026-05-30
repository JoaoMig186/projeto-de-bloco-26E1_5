package com.infnet.kafka;

import com.infnet.events.CustomerCreatedEvent;
import com.infnet.model.Geocode;
import com.infnet.service.GeocodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaGeocodeListener {

    private final GeocodeService service;
    private final KafkaService kafkaService;

    @KafkaListener(topics = "microservices.customer.created")
        public void receiveCustomerCreatedEvent(CustomerCreatedEvent event){
        System.out.println("EVENTO CUSTOMER CREATED RECEBIDO!");
        Geocode geocode = service.getUserGeocode(event.address());
        kafkaService.sendGeocodeEvent(event.userId(),geocode.getLat(),geocode.getLon());
    }


}
