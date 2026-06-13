package com.infnet.kafka;

import com.infnet.dtos.ProductSyncDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, ProductSyncDTO> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);

    public void sendProductSyncEvent(ProductSyncDTO event) {
        // Envia para o tópico "icimento.store.product.sync" usando o ID do produto como chave
        kafkaTemplate.send("icimento.store.product.sync", String.valueOf(event.id()), event);
        log.info("Evento de sincronização do produto '{}' enviado com sucesso para a fila", event.name());
    }
}