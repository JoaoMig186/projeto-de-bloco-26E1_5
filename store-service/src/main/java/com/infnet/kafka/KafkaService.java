package com.infnet.kafka;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);

    private void sendEvent(String event){
        kafkaTemplate.send("icimento.store.product.sync",
                String.valueOf(UUID.randomUUID().toString()),
                event
        );
    }

    public void sendProductSyncEvent(String payload){
        sendEvent(payload);

    }
}