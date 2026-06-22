package com.infnet.kafka;

import com.infnet.dtos.ProductSyncDTO;
import com.infnet.events.StoreCreatedEvent;
import com.infnet.model.Store;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);

    private void sendProductEvent(ProductSyncDTO event){
        kafkaTemplate.send("icimento.store.product.sync",
                String.valueOf(UUID.randomUUID().toString()),
                event
        );
    }

    public void sendProductSyncEvent(ProductSyncDTO dto){
        sendProductEvent(dto);
    }

    private void sendStoreEvent(StoreCreatedEvent event){
        kafkaTemplate.send("icimento.store.created",
                String.valueOf(event.eventId()),
                event
        );
    }

    public void sendStoreCreatedEvent(Long storeId, String address){
        StoreCreatedEvent event = StoreCreatedEvent.createEvent(storeId, address);
        sendStoreEvent(event);
        log.info("Evento de Criação de Loja Enviado com Sucesso");
    }
}