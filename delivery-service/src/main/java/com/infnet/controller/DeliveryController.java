package com.infnet.controller;

import com.infnet.dto.request.delivery.DeliveryRequestDTO;
import com.infnet.dto.response.delivery.DeliveryResponseDTO;
import com.infnet.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService service;

    @PostMapping
    public ResponseEntity<DeliveryResponseDTO> create(
            @Valid @RequestBody DeliveryRequestDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<Page<DeliveryResponseDTO>> toList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                service.toList(page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponseDTO> findById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                service.findById(id)
        );
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<DeliveryResponseDTO> start(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                service.startDelivery(id)
        );
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<DeliveryResponseDTO> finish(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                service.finishDelivery(id)
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<DeliveryResponseDTO> cancel(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                service.cancel(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
