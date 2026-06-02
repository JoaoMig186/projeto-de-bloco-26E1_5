package com.infnet.kafka;

import com.infnet.events.GeocodeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, GeocodeEvent> kafkaTemplate;

    private void sendEvent(GeocodeEvent event){
        kafkaTemplate.send("microservices.geocode.customer.scrape",
                String.valueOf(event.eventId()),
                event
        );
    }

    public void sendGeocodeEvent(Long userId, Double lat, Double lon){
        GeocodeEvent event = GeocodeEvent.createEvent(userId, lat, lon);
        sendEvent(event);
        System.out.println("EVENTO GEOCODE ENVIADO!");
    }


}
