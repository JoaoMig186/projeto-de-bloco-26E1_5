package com.infnet.kafka;

import com.infnet.events.CustomerCreatedEvent;
import com.infnet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, CustomerCreatedEvent> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);


    private void sendEvent(CustomerCreatedEvent event){
        kafkaTemplate.send("icimento.customer.created",
                String.valueOf(event.eventId()),
                event
        );
    }

    public void sendCustomerCreatedEvent(Long userId, String address){
        CustomerCreatedEvent event = CustomerCreatedEvent.createEvent(userId, address);
        sendEvent(event);
        log.info("Evento de Criação de Cliente Enviado com Sucesso");
    }


}
