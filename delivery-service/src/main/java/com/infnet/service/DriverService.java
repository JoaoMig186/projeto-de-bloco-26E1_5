package com.infnet.service;

import com.infnet.domain.entity.Driver;
import com.infnet.domain.entity.enums.VehicleType;
import com.infnet.dto.request.driver.DriverLocationUpdateDTO;
import com.infnet.dto.request.driver.DriverRequestDTO;
import com.infnet.dto.request.driver.DriverUpdateDTO;
import com.infnet.dto.response.driver.DriverResponseDTO;
import com.infnet.exception.ResourceNotFoundException;
import com.infnet.repository.DriverRepository;
import com.infnet.repository.specification.DriverSpecification;
import com.infnet.service.utils.UtilService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(
            DriverRepository driverRepository
    ){
        this.driverRepository = driverRepository;
    }

    public DriverResponseDTO create(DriverRequestDTO dto) {

        Driver driver = Driver.create(
                dto.name(),
                dto.phone(),
                dto.vehicleType()
        );

        driverRepository.save(driver);

        return toResponse(driver);
    }

    @Transactional(readOnly = true)
    public Page<DriverResponseDTO> toList(
            String name,
            int page,
            int size
    ){
        UtilService.validatePaginationParams(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Specification<Driver> specification =
                Specification.where(DriverSpecification.byPartialName(name));

        return driverRepository.findAll(specification, pageable)
                .map(this::toResponse);

    }

    @Transactional(readOnly = true)
    public DriverResponseDTO findById(UUID id){
        Driver driver = findEntity(id);
        return toResponse(driver);
    }

    @Transactional(readOnly = true)
    public Page<DriverResponseDTO> findAvailableDrivers(
            int page,
            int size
    ) {

        UtilService.validatePaginationParams(page, size);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("name").ascending()
        );

        return driverRepository
                .findByAvailableTrue(pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<DriverResponseDTO> findAvailableDriversByVehicle(
            VehicleType vehicleType,
            int page,
            int size
    ) {
        UtilService.validatePaginationParams(page, size);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("name").ascending()
        );

        return driverRepository
                .findByAvailableTrueAndVehicleType(vehicleType, pageable)
                .map(this::toResponse);
    }

    @Transactional
    public DriverResponseDTO update(
            UUID id,
            DriverUpdateDTO dto
    ) {

        Driver driver = findEntity(id);

        driver.update(
                dto.name(),
                dto.phone(),
                dto.vehicleType()
        );

        return toResponse(driver);
    }

    @Transactional
    public DriverResponseDTO setAvailable(
            UUID driverId
    ) {
        Driver driver = findEntity(driverId);

        driver.becomeAvailable();

        return toResponse(driver);
    }

    @Transactional
    public DriverResponseDTO setUnavailable(
            UUID driverId
    ) {
        Driver driver = findEntity(driverId);

        driver.becomeUnavailable();

        return toResponse(driver);
    }

    @Transactional
    public DriverResponseDTO updateLocation (
            UUID driverId,
            DriverLocationUpdateDTO dto
    ) {
        Driver driver = findEntity(driverId);

        driver.updateLocation(
                dto.latitude(),
                dto.longitude()
        );

        return toResponse(driver);
    }

    public DriverResponseDTO delete(UUID id) {

        Driver driver = findEntity(id);

        DriverResponseDTO response = toResponse(driver);

        driverRepository.delete(driver);

        return response;
    }

    private Driver findEntity(UUID id){
        return driverRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Motorista com id " + id + " não encontrado."
                )
            );
    }

    private DriverResponseDTO toResponse(Driver d) {
        return new DriverResponseDTO(
                d.getId(),
                d.getName(),
                d.getPhone(),
                d.getVehicleType(),
                d.getLatitude(),
                d.getLongitude(),
                d.getAvailable()
        );
    }
}
