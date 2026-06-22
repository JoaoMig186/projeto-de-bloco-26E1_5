package com.infnet.controller;

import com.infnet.domain.entity.enums.VehicleType;
import com.infnet.dto.request.driver.DriverLocationUpdateDTO;
import com.infnet.dto.request.driver.DriverRequestDTO;
import com.infnet.dto.request.driver.DriverUpdateDTO;
import com.infnet.dto.response.driver.DriverResponseDTO;
import com.infnet.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
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
            @PathVariable Long id
            ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<Page<DriverResponseDTO>> findAvailableDrivers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findAvailableDrivers(page, size));
    }

    @GetMapping("/available/vehicle")
    public ResponseEntity<Page<DriverResponseDTO>> findAvailableDriversByVehicle(
            @RequestParam VehicleType vehicleType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.findAvailableDriversByVehicle(vehicleType, page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DriverUpdateDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.update(id, dto));
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<DriverResponseDTO> setAvailable(
            @PathVariable Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.setAvailable(id));
    }

    @PatchMapping("/{id}/unavailable")
    public ResponseEntity<DriverResponseDTO> setUnavailable(
            @PathVariable Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.setUnavailable(id));
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<DriverResponseDTO> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody DriverLocationUpdateDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.updateLocation(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}