package com.infnet.controller;

import com.infnet.dtos.StoreRequestDTO;
import com.infnet.dtos.StoreResponseDTO;
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

    // Endpoint para registar uma nova loja parceira
    @PostMapping
    public ResponseEntity<StoreResponseDTO> createStore(@Valid @RequestBody StoreRequestDTO dto) {
        StoreResponseDTO createdStore = storeService.createStore(dto);
        // Retorna o status 201 (Created) em vez do padrão 200 (OK)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStore);
    }

    // Endpoint para listar as lojas disponíveis (para clientes ou integradores)
    @GetMapping
    public ResponseEntity<List<StoreResponseDTO>> getAllActiveStores() {
        return ResponseEntity.ok(storeService.getAllActiveStores());
    }

    // Endpoint para consultar detalhes de uma loja específica
    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> getStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    // Endpoint para inativar uma loja
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateStore(@PathVariable Long id) {
        storeService.deactivateStore(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}