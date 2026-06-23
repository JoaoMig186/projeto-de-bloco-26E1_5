package com.infnet.kafka;

import com.infnet.kafka.events.PaymentApprovatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

//    Para Delivery
    public void sendPaymentApprovatedEvent(PaymentApprovatedEvent event){
        kafkaTemplate.send("payment-approvated-topic", event.orderId().toString(), event);
    }
}
