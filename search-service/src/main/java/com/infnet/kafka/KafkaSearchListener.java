package com.infnet.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.dtos.ProductSyncDTO;
import com.infnet.model.ProductDocument;
import com.infnet.repository.ProductSearchRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaSearchListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaSearchListener.class);
    private final ObjectMapper objectMapper;
    private final ProductSearchRepository productRepository;

    @KafkaListener(topics = "icimento.store.product.sync")
    public void receiveOutboxProductSyncEvent(String payload){

        try{
            ProductSyncDTO product =
                    objectMapper.readValue(payload, ProductSyncDTO.class);

            ProductDocument document = new ProductDocument(
                    product.id(),
                    product.name(),
                    product.description(),
                    product.price(),
                    product.storeId(),
                    product.storeName(),
                    product.category(),
                    product.durability()
            );

            productRepository.save(document);

        } catch (Exception ex){
            log.error("Erro ao processar evento", ex);
            throw new RuntimeException(ex);
        }
    }


}
