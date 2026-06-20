package com.infnet.service;

import com.infnet.domain.entity.Delivery;
import com.infnet.domain.entity.Driver;
import com.infnet.dto.request.freight.FreightRequestDTO;
import com.infnet.dto.request.freight.FreightResponseDTO;
import com.infnet.dto.request.delivery.DeliveryRequestDTO;
import com.infnet.dto.response.delivery.DeliveryResponseDTO;
import com.infnet.exception.ResourceNotFoundException;
import com.infnet.metrics.DeliveryMetrics;
import com.infnet.repository.DeliveryRepository;
import com.infnet.repository.DriverRepository;
import com.infnet.service.freight.FreightService;
import com.infnet.service.utils.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DriverRepository driverRepository;
    private final FreightService freightService;

    private final DeliveryMetrics metrics;
    //private final DeliveryEventProducer producer;

    public DeliveryResponseDTO create(
            DeliveryRequestDTO dto
    ) {
        FreightResponseDTO freight = freightService.calculate(
                new FreightRequestDTO(dto.distanceKm(), dto.weightKg())
        );

        Driver driver = driverRepository.findFirstByAvailableTrueAndVehicleType(
                freight.vehicleType()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Nenhum motorista disponível para o tipo de veículo "
                                + freight.vehicleType()
                )
        );

        Delivery delivery = Delivery.create(
                dto.orderId(),
                dto.originAddress(),
                dto.destinationAddress(),
                dto.distanceKm(),
                freight.estimatedMinutes(),
                freight.freightValue()
        );

        delivery.assignDriver(
                driver.getId()
        );

        driver.becomeUnavailable();
        deliveryRepository.save(delivery);
        publishStatusChanged(delivery);
        metrics.incrementCreated();
        metrics.recordFreight(
                delivery.getShippingPrice()
        );
        return toResponse(delivery);
    }

    @Transactional(readOnly = true)
    public DeliveryResponseDTO findById(UUID id) {
        Delivery delivery = findEntity(id);
        return toResponse(delivery);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryResponseDTO> toList(
            int page,
            int size
    ) {
        UtilService.validatePaginationParams(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return deliveryRepository
                .findAll(pageable)
                .map(this::toResponse);
    }

    public DeliveryResponseDTO startDelivery(UUID deliveryId) {
        Delivery delivery =
                findEntity(deliveryId);

        delivery.startDelivery();
        publishStatusChanged(delivery);
        return toResponse(delivery);
    }

    public DeliveryResponseDTO finishDelivery(UUID deliveryId) {
        Delivery delivery =
                findEntity(deliveryId);

        Driver driver = driverRepository.findById(
                delivery.getDriverId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Motorista não encontrado."
                )
        );

        driver.becomeAvailable();
        delivery.finishDelivery();
        publishStatusChanged(delivery);
        metrics.incrementFinished();
        return toResponse(delivery);
    }

    public DeliveryResponseDTO cancel(UUID deliveryId) {
        Delivery delivery =
                findEntity(deliveryId);

        delivery.cancel();

        if(delivery.getDriverId() != null) {
            Driver driver = driverRepository.findById(
                    delivery.getDriverId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Motorista não encontrado."
                    )
            );
            driver.becomeAvailable();
        }
        publishStatusChanged(delivery);
        metrics.incrementCancelled();
        return toResponse(delivery);
    }

    public void delete(UUID id) {
        Delivery delivery =
                findEntity(id);

        deliveryRepository.delete(delivery);
    }

    private Delivery findEntity(UUID id) {
        return deliveryRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Entrega com id " + id + " não encontrada."
                )
            );
    }

    private DeliveryResponseDTO toResponse(
            Delivery delivery
    ) {

        return new DeliveryResponseDTO(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getDriverId(),
                delivery.getOriginAddress(),
                delivery.getDestinationAddress(),
                delivery.getDistanceKm(),
                delivery.getEstimatedTimeMinutes(),
                delivery.getShippingPrice(),
                delivery.getStatus(),
                delivery.getCreatedAt(),
                delivery.getDeliveredAt()
        );
    }

    private void publishStatusChanged(
            Delivery delivery
    ) {

        /*producer.publishStatusChanged(
                new DeliveryStatusChangedEvent(
                        delivery.getId(),
                        delivery.getOrderId(),
                        delivery.getDriverId(),
                        delivery.getStatus(),
                        LocalDateTime.now()
                )
        );*/
    }
}
