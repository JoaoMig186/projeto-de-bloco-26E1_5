package com.infnet.service;

import com.infnet.dtos.ProductRequestDTO;
import com.infnet.dtos.ProductResponseDTO;
import com.infnet.dtos.ProductSyncDTO;
import com.infnet.model.Product;
import com.infnet.model.Store;
import com.infnet.model.enums.Category;
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

    // Injetaríamos aqui o nosso produtor de mensagens (ex: RabbitTemplate)
    // private final RabbitTemplate rabbitTemplate;

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        // Verifica se a loja existe antes de adicionar o produto
        Store store = storeRepository.findById(dto.storeId())
                .orElseThrow(() -> new RuntimeException("Loja não encontrada para associar o produto."));

        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStockQuantity(dto.stockQuantity());
        product.setCategory(Category.valueOf(dto.category()));
        product.setStore(store);

        product = productRepository.save(product);

        // TODO: Sincronizar com o search-service para a busca fuzzy e geolocalizada
        syncWithSearchService(product, store);

        return new ProductResponseDTO(product);
    }

    public List<ProductResponseDTO> getProductsByStore(Long storeId) {
        return productRepository.findByStoreId(storeId)
                .stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }

    // Método interno para preparar o envio de dados ao Elastic Search
    private void syncWithSearchService(Product product, Store store) {
        ProductSyncDTO syncDTO = new ProductSyncDTO(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                store.getId(),
                store.getName(),
                store.getLatitude(),
                store.getLongitude() // Dados vitais para a geolocalização no Elastic Search
        );

        // Exemplo: rabbitTemplate.convertAndSend("product.exchange", "product.routing.key", syncDTO);
        System.out.println("Sincronizando com Search-Service: " + syncDTO);
    }
}