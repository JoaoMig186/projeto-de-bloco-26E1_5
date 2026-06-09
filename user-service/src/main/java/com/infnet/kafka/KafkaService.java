package com.infnet.kafka;

import com.infnet.events.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, CustomerCreatedEvent> kafkaTemplate;

    private void sendEvent(CustomerCreatedEvent event){
        kafkaTemplate.send("microservices.customer.created",
                String.valueOf(event.eventId()),
                event
        );
    }

    public void sendCustomerCreatedEvent(Long userId, String address){
        CustomerCreatedEvent event = CustomerCreatedEvent.createEvent(userId, address);
        sendEvent(event);
        System.out.println("EVENTO CUSTOMER CREATED ENVIADO!");
    }


}
