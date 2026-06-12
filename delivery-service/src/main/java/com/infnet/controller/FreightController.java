package com.infnet.controller;

import com.infnet.dto.request.Freight.FreightRequestDTO;
import com.infnet.dto.request.Freight.FreightResponseDTO;
import com.infnet.service.freight.FreightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/freight")
@RequiredArgsConstructor
public class FreightController {

    private final FreightService service;

    @PostMapping("/calculate")
    public ResponseEntity<FreightResponseDTO> calculate(
            @RequestBody FreightRequestDTO dto
    ) {

        return ResponseEntity.ok(
                service.calculate(dto)
        );
    }
}
