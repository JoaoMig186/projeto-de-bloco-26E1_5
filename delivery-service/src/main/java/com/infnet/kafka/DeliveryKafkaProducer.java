package com.infnet.kafka;

import com.infnet.events.DeliveryUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "delivery-updated-topic";

    public void sendDeliveryUpdated(DeliveryUpdatedEvent event) {

        log.info(
                "Enviando evento de atualização da entrega. Pedido={}, status={}",
                event.orderId(),
                event.deliveryStatus()
        );

        kafkaTemplate.send(
                TOPIC,
                event.orderId().toString(),
                event
        );
    }
}
