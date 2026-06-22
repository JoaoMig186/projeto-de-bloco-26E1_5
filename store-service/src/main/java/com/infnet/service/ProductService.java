package com.infnet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.dtos.OrderProductInfoDTO;
import com.infnet.dtos.ProductRequestDTO;
import com.infnet.dtos.ProductResponseDTO;
import com.infnet.dtos.ProductSyncDTO;
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
    public ProductResponseDTO createProduct(ProductRequestDTO dto) throws JsonProcessingException {

        Store store = storeRepository.findById(dto.storeId())
                .orElseThrow(() -> new RuntimeException("Loja não encontrada para associar o produto."));

        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStockQuantity(dto.stockQuantity());

        product.setCategory(Category.valueOf(dto.category().toUpperCase()));

        product.setDurability(
                Durability.valueOf(dto.durability().trim().toUpperCase())
        );

        product.setWeight(dto.weight());
        product.setStore(store);

        product = productRepository.save(product);

        ProductSyncDTO productDTO = ProductSyncDTO.fromDomain(product);

        String payload = objectMapper.writeValueAsString(productDTO);

        OutboxProductEvent event = new OutboxProductEvent(
                product.getId(),
                ProductEventType.PRODUCT_CREATED,
                payload
        );

        outboxRepository.save(event);

        storeMetrics.incrementProductCreation();

        storeMetrics.incrementProductCategoryCounter(product.getCategory().name());

        return new ProductResponseDTO(product);
    }

    public List<ProductResponseDTO> getProductsByStore(Long storeId) {
        return productRepository.findByStoreId(storeId)
                .stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }
    public OrderProductInfoDTO getProductInfoForOrder(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        Store store = product.getStore();

        boolean isFragile = product.getDurability() == Durability.FRAGIL;

        OrderProductInfoDTO.StoreDTO storeDTO = new OrderProductInfoDTO.StoreDTO(
                store.getId(),
                store.getName(),
                store.getLatitude(),
                store.getLongitude()
        );

        return new OrderProductInfoDTO(
                product.getId(),
                store.getId(),
                product.getName(),
                product.getPrice(),
                product.getWeight(),
                isFragile,
                storeDTO
        );
    }
}