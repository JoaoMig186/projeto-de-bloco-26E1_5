package com.infnet.domain;

import com.infnet.domain.enums.DeliveryStatus;
import com.infnet.domain.enums.OrderStatus;
import com.infnet.domain.enums.PaymentMethod;
import com.infnet.domain.enums.PaymentStatus;
import com.infnet.dto.cart.ItemCarrinhoResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idUser;

    private Long idStore;

    private Long idCart;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

//    Payment
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private List<ItemCarrinhoResponseDTO> productList;

    private BigDecimal productsPrice;

    private BigDecimal shippingPrice;

    private BigDecimal totalPrice;

//    Delivery
    private String vehicleType;

    private Double freightValue;

    private Integer estimatedMinutes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    protected Order() {}

    public Order(Long idUser,
                 Long idStore,
                 Long idCart,
                 PaymentMethod paymentMethod) {
        this.idUser = idUser;
        this.idCart = idCart;
        this.idStore = idStore;
        this.paymentMethod = paymentMethod;
    }
}
