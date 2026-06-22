package com.infnet.kafka;

import com.infnet.events.CustomerCreatedEvent;
import com.infnet.events.StoreCreatedEvent;
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
        kafkaService.sendUserGeocodeEvent(event.userId(),geocode.getLat(),geocode.getLon());
    }

    @KafkaListener(topics = "icimento.store.created", groupId = "geocode-group")
    public void receiveStoreCreatedEvent(StoreCreatedEvent event) {
        log.info("Evento de Criação de Loja recebido com sucesso");

        // Simula o cálculo do geocode baseado no endereço da loja
        Geocode geocode = service.getStoreGeocode(event.address());

        // Retorna o evento para o store-service (você precisará criar este método no KafkaService do geocode)
        kafkaService.sendStoreGeocodeEvent(event.storeId(), geocode.getLat(), geocode.getLon());
    }


}
