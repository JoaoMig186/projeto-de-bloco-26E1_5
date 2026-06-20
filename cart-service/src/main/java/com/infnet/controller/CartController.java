package com.infnet.controller;

import com.infnet.DTO.AddItemDTO;
import com.infnet.DTO.PagamentoIniciadoEvent;
import com.infnet.domain.Cart;
import com.infnet.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrinhos")
@RequiredArgsConstructor
public class CartController {

    private final CarService service;

    @PostMapping
    public ResponseEntity<Cart> createCart(
            @RequestHeader("X-User-Id") Long userId
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createCart(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ) {

        return ResponseEntity.ok(
                service.getCart(id, userId)
        );
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<Cart> addItem(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody AddItemDTO dto
    ) {

        return ResponseEntity.ok(
                service.addItem(id, userId, dto)
        );
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Cart> removeItem(
            @PathVariable Long id,
            @PathVariable Long itemId,
            @RequestHeader("X-User-Id") Long userId
    ) {

        return ResponseEntity.ok(
                service.removeItem(id, userId, itemId)
        );
    }

    @DeleteMapping("/{id}/clear")
    public ResponseEntity<Cart> clearCart(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ) {

        return ResponseEntity.ok(
                service.clearCart(id, userId)
        );
    }

    @GetMapping("/order")
    public ResponseEntity<PagamentoIniciadoEvent> order(
            @RequestHeader("X-User-Id") Long usuarioId
    ) {
        return ResponseEntity.ok(
                service.buscarDadosPagamento( usuarioId)
        );
    }
}