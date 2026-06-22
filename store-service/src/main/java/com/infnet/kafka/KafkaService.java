package com.infnet.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.events.StoreCreatedEvent;
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
    private final ObjectMapper objectMapper; // Injetado para serializar o record em JSON
    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);

    // Ajustei o método privado para receber o tópico dinamicamente
    private void sendEvent(String topic, String event){
        kafkaTemplate.send(topic,
                String.valueOf(UUID.randomUUID().toString()),
                event
        );
    }

    public void sendProductSyncEvent(String payload){
        sendEvent("icimento.store.product.sync", payload);
    }

    // NOVO MÉTODO PARA A INTEGRAÇÃO COM O GEOCODE
    public void sendStoreCreatedEvent(StoreCreatedEvent event) {
        try {
            // Converte o objeto do evento para uma String JSON
            String payload = objectMapper.writeValueAsString(event);

            // Envia para o tópico que o geocode-fake-service está escutando
            sendEvent("icimento.store.created", payload);
            log.info("Evento StoreCreatedEvent enviado com sucesso para a loja ID: {}", event.storeId());

        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar StoreCreatedEvent", e);
            throw new RuntimeException("Falha ao enviar evento para o Kafka", e);
        }
    }
}