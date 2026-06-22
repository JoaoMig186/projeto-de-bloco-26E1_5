package com.infnet.domain.entity;

import com.infnet.domain.entity.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long orderId;

    private Long driverId;

    private String originAddress;

    private String destinationAddress;

    private Double distanceKm;

    private Integer estimatedTimeMinutes;

    private Double shippingPrice;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime deliveredAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static Delivery create(
            Long orderId,
            String originAddress,
            String destinationAddress,
            Double distanceKm,
            Integer estimatedTimeMinutes,
            Double shippingPrice
    ) {

        if(distanceKm <= 0) {
            throw new IllegalArgumentException(
                    "Distância deve ser maior que zero."
            );
        }

        if(shippingPrice < 0) {
            throw new IllegalArgumentException(
                    "Valor do frete inválido."
            );
        }

        Delivery delivery = new Delivery();

        delivery.orderId = orderId;
        delivery.originAddress = originAddress;
        delivery.destinationAddress = destinationAddress;
        delivery.distanceKm = distanceKm;
        delivery.estimatedTimeMinutes = estimatedTimeMinutes;
        delivery.shippingPrice = shippingPrice;
        delivery.status = DeliveryStatus.CREATED;

        return delivery;
    }

    public void assignDriver(Long driverId) {

        if(this.status != DeliveryStatus.CREATED) {
            throw new IllegalStateException(
                    "Motorista só pode ser atribuído a entregas criadas."
            );
        }

        if(this.driverId != null) {
            throw new IllegalStateException(
                    "Entrega já possui motorista."
            );
        }

        this.driverId = driverId;
        this.status = DeliveryStatus.DRIVER_ASSIGNED;
    }

    public void startDelivery() {

        if(this.status != DeliveryStatus.DRIVER_ASSIGNED) {
            throw new IllegalStateException(
                    "A entrega não pode ser iniciada."
            );
        }

        this.status = DeliveryStatus.IN_TRANSIT;
    }

    public void finishDelivery() {

        if(this.status != DeliveryStatus.IN_TRANSIT) {
            throw new IllegalStateException(
                    "A entrega não está em trânsito."
            );
        }

        this.status = DeliveryStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public void cancel() {

        if(
                this.status == DeliveryStatus.DELIVERED ||
                        this.status == DeliveryStatus.CANCELLED
        ) {
            throw new IllegalStateException(
                    "Não é possível cancelar a entrega."
            );
        }

        this.status = DeliveryStatus.CANCELLED;
    }
}
