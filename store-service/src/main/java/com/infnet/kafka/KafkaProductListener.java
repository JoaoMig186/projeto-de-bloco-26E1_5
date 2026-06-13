package com.infnet.kafka;

import com.infnet.events.ProductSyncEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProductListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaProductListener.class);
    // Posteriormente, você irá injetar o SearchService aqui para salvar no Elasticsearch

    @KafkaListener(topics = "icimento.store.product.sync")
    public void receiveProductSyncEvent(ProductSyncEvent event) {
        log.info("Evento Recebido - Preparando para indexar produto: '{}' (Loja: {})", event.name(), event.storeName());

        // TODO: Mapear o ProductSyncEvent para a Entidade do Elasticsearch e usar o repository do search-service para salvar.
    }
}