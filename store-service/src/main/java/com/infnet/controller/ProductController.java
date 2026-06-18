package com.infnet.controller;

import com.infnet.dtos.ProductRequestDTO;
import com.infnet.dtos.ProductResponseDTO;
import com.infnet.model.outbox.OutboxProductEvent;
import com.infnet.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Endpoint para a loja adicionar um novo produto ao seu catálogo
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO dto) {
        ProductResponseDTO createdProduct = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // Endpoint para listar todo o catálogo de uma loja específica
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(productService.getProductsByStore(storeId));
    }

    //TESTE APAGAR DPS
    @GetMapping("/events")
    public ResponseEntity<List<OutboxProductEvent>> getEvents(){
        return ResponseEntity.ok(productService.getEvents());
    }

}