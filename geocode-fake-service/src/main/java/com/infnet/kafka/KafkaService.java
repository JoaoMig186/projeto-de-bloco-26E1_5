package com.infnet.kafka;

import com.infnet.events.GeocodeEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, GeocodeEvent> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);
    private void sendEvent(GeocodeEvent event){
        kafkaTemplate.send("icimento.geocode.customer.scrape",
                String.valueOf(event.eventId()),
                event
        );
    }

    public void sendGeocodeEvent(Long userId, Double lat, Double lon){
        GeocodeEvent event = GeocodeEvent.createEvent(userId, lat, lon);
        sendEvent(event);
        log.info("Evento Geocode do Cliente enviado com sucesso");
    }


}
