package com.infnet.controller;

import com.infnet.DTO.AddItemDTO;
import com.infnet.DTO.PagamentoIniciadoEvent;
import com.infnet.domain.Cart;
import com.infnet.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @PostMapping
    public ResponseEntity<Cart> createCart(
            @RequestHeader(value = "X-User-Id", required = true) Long userId
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createCart(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(
            @PathVariable("id") Long id,
            @RequestHeader(name = "X-User-Id", required = true) Long userId    ) {

        return ResponseEntity.ok(
                service.getCart(id, userId)
        );
    }
    @PostMapping("/{id}/items")
    public ResponseEntity<Cart> addItem(
            @PathVariable("id") Long id,
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @RequestBody AddItemDTO dto
    ) {

        return ResponseEntity.ok(
                service.addItem(id, userId, dto)
        );
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Cart> removeItem(
            @PathVariable("id") Long id,
            @PathVariable("itemId") Long itemId,
            @RequestHeader(name = "X-User-Id", required = true) Long userId
    ) {

        return ResponseEntity.ok(
                service.removeItem(id, userId, itemId)
        );
    }
    @PostMapping("/{id}/finalize")
    public ResponseEntity<Cart> finalizeCart(
            @PathVariable("id") Long id,
            @RequestHeader(name = "X-User-Id", required = true) Long userId
    ) {
        return ResponseEntity.ok(
                service.finalizeCart(id, userId)
        );
    }
    @DeleteMapping("/{id}/clear")
    public ResponseEntity<Cart> clearCart(
            @PathVariable("id") Long id,
            @RequestHeader(name = "X-User-Id", required = true) Long userId
    ) {

        return ResponseEntity.ok(
                service.clearCart(id, userId)
        );
    }

    @GetMapping("/order")
    public ResponseEntity<PagamentoIniciadoEvent> order(
            @RequestHeader(name = "X-User-Id", required = true) Long userId
    ) {
        return ResponseEntity.ok(
                service.getPaymentData( userId)
        );
    }
}