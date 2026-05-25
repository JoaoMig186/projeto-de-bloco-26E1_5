package com.infnet.delivery_sevice.controller;

import com.infnet.delivery_sevice.dto.request.DriverRequestDTO;
import com.infnet.delivery_sevice.dto.response.DriverResponseDTO;
import com.infnet.delivery_sevice.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService service;

    @PostMapping
    public ResponseEntity<DriverResponseDTO> create(
            @Valid @RequestBody DriverRequestDTO dto){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<Page<DriverResponseDTO>> toList(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                service.toList(
                        name,
                        page,
                        size
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> findById(
            @PathVariable UUID id
            ){
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }
}