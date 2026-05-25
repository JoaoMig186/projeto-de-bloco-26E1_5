package com.infnet.delivery_sevice.service;

import com.infnet.delivery_sevice.domain.entity.Driver;
import com.infnet.delivery_sevice.domain.entity.enums.VehicleType;
import com.infnet.delivery_sevice.dto.request.DriverRequestDTO;
import com.infnet.delivery_sevice.dto.response.DriverResponseDTO;
import com.infnet.delivery_sevice.repository.DriverRepository;
import com.infnet.delivery_sevice.repository.specification.DriverSpecification;
import com.infnet.delivery_sevice.service.utils.UtilService;
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

        Driver driver = Driver.builder(
                dto.name(),
                dto.phone(),
                dto.vehicleType()
        );

        driverRepository.save(driver);

        return new DriverResponseDTO(
                driver.getId(),
                driver.getName(),
                driver.getVehicleType(),
                driver.getAvailable()
        );
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
                .map(this::toSummaryResponse);

    }

    @Transactional(readOnly = true)
    public DriverResponseDTO findById(UUID id){
        Driver driver = findEntity(id);

        return new DriverResponseDTO(
                driver.getId(),
                driver.getName(),
                driver.getVehicleType(),
                driver.getAvailable()
        );
    }

    private Driver findEntity(UUID id){
        return driverRepository.findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Driver com o id: " + id + "não encontrado."
                ));
    }

    private DriverResponseDTO toSummaryResponse(Driver d) {
        return new DriverResponseDTO(
                d.getId(),
                d.getName(),
                d.getVehicleType(),
                d.getAvailable()
        );
    }
}
