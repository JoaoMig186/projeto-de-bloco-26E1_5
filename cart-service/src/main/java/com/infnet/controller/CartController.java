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

    @GetMapping
    public ResponseEntity<Cart> getCart(
            @RequestHeader(name = "X-User-Id", required = true) Long userId    ) {

        return ResponseEntity.ok(
                service.getCart(userId)
        );
    }
    @PostMapping("/items")
    public ResponseEntity<Cart> addItem(
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @RequestBody AddItemDTO dto
    ) {

        return ResponseEntity.ok(
                service.addItem( userId, dto)
        );
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Cart> removeItem(
            @PathVariable("itemId") Long itemId,
            @RequestHeader(name = "X-User-Id", required = true) Long userId
    ) {

        return ResponseEntity.ok(
                service.removeItem( userId, itemId)
        );
    }
    @PostMapping("/finalize")
    public ResponseEntity<Cart> finalizeCart(
            @RequestHeader(name = "X-User-Id", required = true) Long userId
    ) {
        return ResponseEntity.ok(
                service.finalizeCart( userId)
        );
    }
    @DeleteMapping("/clear")
    public ResponseEntity<Cart> clearCart(
            @RequestHeader(name = "X-User-Id", required = true) Long userId
    ) {

        return ResponseEntity.ok(
                service.clearCart( userId)
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