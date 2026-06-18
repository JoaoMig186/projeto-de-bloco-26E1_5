package com.infnet.controller;

import com.infnet.dtos.ProductRequestDTO;
import com.infnet.dtos.ProductResponseDTO;
import com.infnet.dtos.StoreRequestDTO;
import com.infnet.dtos.StoreResponseDTO;
import com.infnet.service.ProductService;
import com.infnet.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<StoreResponseDTO> createStore(@Valid @RequestBody StoreRequestDTO dto) {
        StoreResponseDTO createdStore = storeService.createStore(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStore);
    }

    @GetMapping
    public ResponseEntity<List<StoreResponseDTO>> getAllActiveStores() {
        return ResponseEntity.ok(storeService.getAllActiveStores());
    }

    // AQUI: Adicionado ("id")
    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> getStoreById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    // AQUI: Adicionado ("id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateStore(@PathVariable("id") Long id) {
        storeService.deactivateStore(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO dto) {
        ProductResponseDTO createdProduct = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // AQUI: Adicionado ("storeId")
    @GetMapping("/{storeId}/products")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByStore(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(productService.getProductsByStore(storeId));
    }
}