package com.infnet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.dtos.ProductRequestDTO;
import com.infnet.dtos.ProductResponseDTO;
import com.infnet.dtos.ProductSyncDTO;
import com.infnet.kafka.KafkaService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final StoreMetrics storeMetrics;
    private final ObjectMapper objectMapper;
    private final OutboxProductEventRepository outboxRepository;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Store store = storeRepository.findById(dto.storeId())
                .orElseThrow(() -> new RuntimeException("Loja não encontrada para associar o produto."));

        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStockQuantity(dto.stockQuantity());

        // Blindagem aplicada apenas na categoria (converte qualquer entrada para maiúsculas)
        product.setCategory(Category.valueOf(dto.category().toUpperCase()));

        // Durabilidade continua com formatação estrita
        product.setDurability(Durability.valueOf(dto.durability()));
        product.setStore(store);

        product = productRepository.save(product);

        JsonNode payload = objectMapper.valueToTree(
                ProductSyncDTO.fromDomain(product)
        );

        OutboxProductEvent event = new OutboxProductEvent(
                product.getId(),
                ProductEventType.PRODUCT_CREATED,
                payload
        );
        outboxRepository.save(event);

        storeMetrics.incrementProductCreation(product.getCategory().name());;

        return new ProductResponseDTO(product);
    }

    public List<ProductResponseDTO> getProductsByStore(Long storeId) {
        return productRepository.findByStoreId(storeId)
                .stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }

//    private void syncWithSearchService(Product product, Store store) {
//        long startTime = System.currentTimeMillis();
//        ProductSyncDTO syncDTO = new ProductSyncDTO(
//                product.getId(),
//                product.getName(),
//                product.getCategory(),
//                product.getPrice(),
//                store.getId(),
//                store.getName(),
//                store.getLatitude(),
//                store.getLongitude()
//        );
//
//        kafkaService.sendProductSyncEvent(syncDTO);
//
//        long endTime = System.currentTimeMillis();
//        storeMetrics.recordKafkaSyncTime(endTime - startTime);
//    }
}