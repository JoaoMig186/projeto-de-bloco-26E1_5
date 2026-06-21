package com.infnet.kafka;

import com.infnet.events.CustomerCreatedEvent;
import com.infnet.model.Geocode;
import com.infnet.service.GeocodeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaGeocodeListener {

    private final GeocodeService service;
    private final KafkaService kafkaService;
    private static final Logger log = LoggerFactory.getLogger(KafkaGeocodeListener.class);

    @KafkaListener(topics = "icimento.customer.created")
        public void receiveCustomerCreatedEvent(CustomerCreatedEvent event){
        log.info("Evento de Criação de Cliente recebido com sucesso");
        Geocode geocode = service.getUserGeocode(event.address());
        kafkaService.sendGeocodeEvent(event.userId(),geocode.getLat(),geocode.getLon());
    }


}
