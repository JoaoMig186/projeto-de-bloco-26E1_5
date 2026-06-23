package com.infnet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.infnet.dtos.*;
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

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> getStoreById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateStore(@PathVariable("id") Long id) {
        storeService.deactivateStore(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO dto) throws JsonProcessingException {
        ProductResponseDTO createdProduct = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping("/{storeId}/products")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByStore(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(productService.getProductsByStore(storeId));
    }

    @GetMapping("/products/{productId}/order-info")
    public ResponseEntity<OrderProductInfoDTO> getProductInfoForOrder(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getProductInfoForOrder(productId));
    }

    @GetMapping("/{id}/geocode")
    public ResponseEntity<GeocodeResponseDTO> getStoreGeocode(@PathVariable("id") Long id) {
        GeocodeResponseDTO geocode = storeService.getStoreGeocode(id);
        return ResponseEntity.ok(geocode);
    }
}