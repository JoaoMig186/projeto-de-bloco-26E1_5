package com.infnet.kafka;

import com.infnet.events.DeliveryUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "delivery-updated-topic";

    public void sendDeliveryUpdated(DeliveryUpdatedEvent event) {
        kafkaTemplate.send(
                TOPIC,
                event.orderId().toString(),
                event
        );
    }
}
