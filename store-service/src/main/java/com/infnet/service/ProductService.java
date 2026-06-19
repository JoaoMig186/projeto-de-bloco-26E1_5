package com.infnet.service;

import com.infnet.dtos.OrderProductInfoDTO;
import com.infnet.dtos.ProductRequestDTO;
import com.infnet.dtos.ProductResponseDTO;
import com.infnet.dtos.ProductSyncDTO;
import com.infnet.kafka.KafkaService;
import com.infnet.metrics.StoreMetrics;
import com.infnet.model.Product;
import com.infnet.model.Store;
import com.infnet.model.enums.Category;
import com.infnet.model.enums.Durability;
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
    private final KafkaService kafkaService;
    private final StoreMetrics storeMetrics;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Store store = storeRepository.findById(dto.storeId())
                .orElseThrow(() -> new RuntimeException("Loja não encontrada para associar o produto."));

        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStockQuantity(dto.stockQuantity());
        product.setWeight(dto.weight());

        // Blindagem aplicada apenas na categoria (converte qualquer entrada para maiúsculas)
        product.setCategory(Category.valueOf(dto.category().toUpperCase()));

        // Durabilidade continua com formatação estrita
        product.setDurability(Durability.valueOf(dto.durability()));
        product.setStore(store);

        product = productRepository.save(product);

        // Sincronizando com o search-service
        syncWithSearchService(product, store);

        storeMetrics.incrementProductCreation(product.getCategory().name());

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

        // Verifica a durabilidade
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
                product.getWeight(),
                isFragile,
                storeDTO
        );
    }

    private void syncWithSearchService(Product product, Store store) {
        long startTime = System.currentTimeMillis();
        ProductSyncDTO syncDTO = new ProductSyncDTO(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                store.getId(),
                store.getName(),
                store.getLatitude(),
                store.getLongitude()
        );

        kafkaService.sendProductSyncEvent(syncDTO);

        long endTime = System.currentTimeMillis();
        storeMetrics.recordKafkaSyncTime(endTime - startTime);
    }
}