package com.infnet.kafka;


import com.infnet.events.StoreGeocodeEvent;
import com.infnet.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaStoreListener {

    private final StoreRepository repository;
    private static final Logger log = LoggerFactory.getLogger(KafkaStoreListener.class);

    @KafkaListener(topics = "icimento.geocode.store.scrape")
    public void receiveGeocodeEvent(StoreGeocodeEvent event) {
        log.info("Evento Geocode da Loja Recebido com Sucesso para Store ID: {}", event.storeId());

        repository.findById(event.storeId()).ifPresent(store -> {
            store.setLatitude(event.lat());
            store.setLongitude(event.lon());
            repository.save(store);

            log.info("Loja '{}' atualizada com Latitude: {} e Longitude: {}", store.getName(), event.lat(), event.lon());
        });
    }
}


