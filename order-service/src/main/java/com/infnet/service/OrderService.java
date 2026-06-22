package com.infnet.service;

import com.infnet.domain.ItemOrder;
import com.infnet.domain.Order;
import com.infnet.domain.enums.DeliveryStatus;
import com.infnet.domain.enums.OrderStatus;
import com.infnet.domain.enums.PaymentStatus;
import com.infnet.dto.OrderRequestDTO;
import com.infnet.dto.cart.PagamentoIniciadoResponseDTO;
import com.infnet.dto.delivery.DeliveryShipResponse;
import com.infnet.dto.delivery.FreightRequestDTO;
import com.infnet.dto.store.GeocodeResponseDTO;
import com.infnet.excepton.OrderNotFoundException;
import com.infnet.kafka.KafkaProducerService;
import com.infnet.kafka.events.OrderCreatedEvent;
import com.infnet.kafka.events.PaymentApprovatedEvent;
import com.infnet.repository.OrderRepository;
import com.infnet.service.cart.CartService;
import com.infnet.service.delivery.DeliveryService;
import com.infnet.service.store.StoreService;
import com.infnet.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DeliveryService deliveryService;
    private final CartService cartService;
    private final UserService userService;
    private final StoreService storeService;
    private final KafkaProducerService kafkaProducerService;

public Order registerOrder(OrderRequestDTO request){

    PagamentoIniciadoResponseDTO cart = cartService.getCart(request.idUser());

    System.out.println("===== CARRINHO =====");
    System.out.println("Carrinho ID: " + cart.carrinhoId());
    System.out.println("Valor Total: " + cart.valorTotal());
    System.out.println("Peso Total: " + cart.valorTotalKg());
    System.out.println("Quantidade de itens: " + cart.itens().size());

    cart.itens().forEach(item -> {
        System.out.println("----- ITEM -----");
        System.out.println("Item ID: " + item.itemId());
        System.out.println("Store ID: " + item.storeId());
        System.out.println("Produto: " + item.productName());
        System.out.println("Quantidade: " + item.quantity());
        System.out.println("Peso: " + item.weight());
        System.out.println("Frágil: " + item.fragile());
    });
    Order order = new Order(
            request.idUser(),
            request.idStore(),
            cart.carrinhoId(),
            request.paymentMethod()
    );

    List<ItemOrder> items = cart.itens().stream()
                    .map(
                            dto -> new ItemOrder(
                                    dto.itemId(),
                                    dto.storeId(),
                                    dto.productName(),
                                    dto.fragile(),
                                    dto.weight(),
                                    dto.quantity(),
                                    order
                            )
                    ).toList();

    order.setProductsPrice(cart.valorTotal());
    order.setProductList(items);

    GeocodeResponseDTO geocodeUser = userService.getGeocode(request.idUser());
    GeocodeResponseDTO geocodeStore = storeService.getGeocode(request.idStore());

    FreightRequestDTO dto = new FreightRequestDTO(
            haversine(geocodeStore.lat(), geocodeStore.lon(), geocodeUser.lat(), geocodeUser.lon()), // passar a lat1, lat2, lon1, lon2
            cart.valorTotalKg()
    );

    DeliveryShipResponse deliveryShipResponse = deliveryService.getDeliveryPrice(dto);
    order.setShippingPrice(deliveryShipResponse.freightValue());

    BigDecimal totalPrice = order.getProductsPrice().add(order.getShippingPrice());
    order.setTotalPrice(totalPrice);

    Order saved = orderRepository.save(order);

    kafkaProducerService.sendOrderCreatedEvent(
            new OrderCreatedEvent(
                    saved.getId(),
                    saved.getIdStore(),
                    saved.getIdUser(),
                    saved.getTotalPrice(),
                    saved.getPaymentMethod()
            )
    );

    return saved;
}

//  ✅ Metodo Kafka
    @Transactional
    public void updateStatusPayment(Long orderId, String paymentStatus){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        PaymentStatus status = PaymentStatus.valueOf(paymentStatus);

        if (status == PaymentStatus.APPROVED) {
            kafkaProducerService.sendPaymentApprovatedEvent(
                    new PaymentApprovatedEvent(
                            order.getId(), true
                    )
            );
            order.setPaymentStatus(status);
        } else {
            order.setPaymentStatus(status);
        }

    }


//    ✅ Metodo Kafka
    @Transactional
    public void updateStatusDelivery(Long orderId, String deliveryStatus){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        DeliveryStatus status = DeliveryStatus.valueOf(deliveryStatus);
        order.setDeliveryStatus(status);
    }

public Order findById(Long id){
    return orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException("Order not found"));
}

public List<Order> findAll(){
    return orderRepository.findAll();
}

public void deleteById(Long id){
    orderRepository.deleteById(id);
}

// Helpers
    public static Double haversine(Double lat1, Double lon1, Double lat2, Double lon2){
        final Double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
