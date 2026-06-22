package com.infnet.kafka;

import com.infnet.events.UserGeocodeEvent;
import com.infnet.events.StoreGeocodeEvent; // Adicionar import
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {
    // Trocado de <String, GeocodeEvent> para <String, Object> para suportar múltiplos eventos
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);

    // --- FLUXO DO CLIENTE ---
    private void sendUserEvent(UserGeocodeEvent event){
        kafkaTemplate.send("icimento.geocode.customer.scrape",
                String.valueOf(event.eventId()),
                event
        );
    }

    public void sendUserGeocodeEvent(Long userId, Double lat, Double lon){
        UserGeocodeEvent event = UserGeocodeEvent.createEvent(userId, lat, lon);
        sendUserEvent(event);
        log.info("Evento Geocode do Cliente enviado com sucesso");
    }

    // --- FLUXO DA LOJA ---
    private void sendStoreEvent(StoreGeocodeEvent event){
        kafkaTemplate.send("icimento.geocode.store.scrape",
                String.valueOf(event.eventId()),
                event
        );
    }

    public void sendStoreGeocodeEvent(Long storeId, Double lat, Double lon){
        StoreGeocodeEvent event = StoreGeocodeEvent.createEvent(storeId, lat, lon);
        sendStoreEvent(event);
        log.info("Evento Geocode da Loja enviado com sucesso para storeId: {}", storeId);
    }
}