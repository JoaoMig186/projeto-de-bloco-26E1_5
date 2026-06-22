package com.infnet.service;

import com.infnet.domain.entity.Delivery;
import com.infnet.domain.entity.Driver;
import com.infnet.domain.entity.enums.DeliveryStatus;
import com.infnet.dto.request.freight.FreightRequestDTO;
import com.infnet.dto.request.freight.FreightResponseDTO;
import com.infnet.dto.request.delivery.DeliveryRequestDTO;
import com.infnet.dto.response.delivery.DeliveryResponseDTO;
import com.infnet.events.DeliveryUpdatedEvent;
import com.infnet.exception.ResourceNotFoundException;
import com.infnet.kafka.DeliveryKafkaProducer;
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

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DriverRepository driverRepository;
    private final FreightService freightService;

    private final DeliveryMetrics metrics;

    private final DeliveryKafkaProducer producer;

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
        metrics.incrementCreated();
        metrics.recordFreight(
                delivery.getShippingPrice()
        );
        return toResponse(delivery);
    }

    @Transactional(readOnly = true)
    public DeliveryResponseDTO findById(Long id) {
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

    public DeliveryResponseDTO startDelivery(Long deliveryId) {
        Delivery delivery =
                findEntity(deliveryId);

        producer.sendDeliveryUpdated(
                new DeliveryUpdatedEvent(
                        delivery.getOrderId(),
                        delivery.getStatus().name()
                )
        );

        delivery.startDelivery();
        return toResponse(delivery);
    }

    public DeliveryResponseDTO finishDelivery(Long deliveryId) {
        Delivery delivery =
                findEntity(deliveryId);

        Driver driver = driverRepository.findById(
                delivery.getDriverId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Motorista não encontrado."
                )
        );

        producer.sendDeliveryUpdated(
                new DeliveryUpdatedEvent(
                        delivery.getOrderId(),
                        delivery.getStatus().name()
                )
        );

        driver.becomeAvailable();
        delivery.finishDelivery();
        metrics.incrementFinished();
        return toResponse(delivery);
    }

    public DeliveryResponseDTO cancel(Long deliveryId) {
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

        producer.sendDeliveryUpdated(
                new DeliveryUpdatedEvent(
                        delivery.getOrderId(),
                        delivery.getStatus().name()
                )
        );

        metrics.incrementCancelled();
        return toResponse(delivery);
    }

    public void delete(Long id) {
        Delivery delivery =
                findEntity(id);

        deliveryRepository.delete(delivery);
    }

    @Transactional
    public void startDeliveryFromPayment(Long orderId) {

        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Entrega não localizada pelo pedido " + orderId)
                );

        if (delivery.getStatus() == DeliveryStatus.IN_TRANSIT) {
            return;
        }

        delivery.startDelivery();
    }

    private Delivery findEntity(Long id) {
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
}
