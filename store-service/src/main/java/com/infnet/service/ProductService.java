package com.infnet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.dtos.ProductCreatedPayloadEvent;
import com.infnet.dtos.ProductRequestDTO;
import com.infnet.dtos.ProductResponseDTO;
import com.infnet.dtos.ProductSyncDTO;
import com.infnet.kafka.KafkaService; // Importar o KafkaService
import com.infnet.metrics.StoreMetrics;
import com.infnet.model.Product;
import com.infnet.model.Store;
import com.infnet.model.enums.Category;
import com.infnet.model.enums.Durability;
import com.infnet.model.enums.ProductEventType;
import com.infnet.model.outbox.OutboxProductEvent;
import com.infnet.repository.OutboxProductEventRepository;
import com.infnet.repository.ProductRepository;
import com.infnet.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.SerializationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final StoreMetrics storeMetrics; // Injetar a métrica
    private final ObjectMapper objectMapper;
    private final OutboxProductEventRepository outboxRepository;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto){
        Store store = storeRepository.findById(dto.storeId())
                .orElseThrow(() -> new RuntimeException("Loja não encontrada para associar o produto."));

        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStockQuantity(dto.stockQuantity());
        product.setCategory(Category.valueOf(dto.category()));
        product.setDurability(Durability.valueOf(dto.durability()));
        product.setStore(store);

        product = productRepository.save(product);

        ProductCreatedPayloadEvent productEventPayload = ProductCreatedPayloadEvent.toProductCreatedPayloadEvent(product);
        try {
            String payload = objectMapper.writeValueAsString(productEventPayload);
            outboxRepository.save(
                    new OutboxProductEvent(
                            product.getId(),
                            ProductEventType.PRODUCT_CREATED,
                            payload
                    )
            );
        } catch (JsonProcessingException e) {
            throw new SerializationException("Erro ao serializar evento", e);
        }

        storeMetrics.incrementProductCreation();

        return new ProductResponseDTO(product);
    }

    public List<ProductResponseDTO> getProductsByStore(Long storeId) {
        return productRepository.findByStoreId(storeId)
                .stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }

    //TESTE APAGAR DPS
    public List<OutboxProductEvent> getEvents() {
        return outboxRepository.findAll();
    }
}