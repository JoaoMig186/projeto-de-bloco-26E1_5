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
import com.infnet.metrics.OrderMetrics;
import com.infnet.repository.OrderRepository;
import com.infnet.service.cart.CartService;
import com.infnet.service.delivery.DeliveryService;
import com.infnet.service.store.StoreService;
import com.infnet.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final OrderMetrics orderMetrics;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

// Com metrica
public Order registerOrder(OrderRequestDTO request, Long userId){
    log.info("[ORDER] Iniciando criação de pedido - userId={}, idStore={}", userId, request.idStore());
    return orderMetrics.medirTempoDePedido(() -> {

        PagamentoIniciadoResponseDTO cart = cartService.getCart(userId);

        log.info("[ORDER] Carrinho obtido - carrinhoId={}, total={}, itens={}",
                cart.carrinhoId(), cart.valorTotal(), cart.itens().size());
        Order order = new Order(
                userId,
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

        GeocodeResponseDTO geocodeUser = userService.getGeocode(userId);
        GeocodeResponseDTO geocodeStore = storeService.getGeocode(request.idStore());

        FreightRequestDTO dto = new FreightRequestDTO(
                haversine(geocodeStore.lat(), geocodeStore.lon(), geocodeUser.lat(), geocodeUser.lon()),
                cart.valorTotalKg()
        );

        DeliveryShipResponse deliveryShipResponse = deliveryService.getDeliveryPrice(dto);
        log.info("[ORDER] Frete calculado - valor={}", deliveryShipResponse.freightValue());
        order.setShippingPrice(deliveryShipResponse.freightValue());
        order.setEstimatedMinutes(deliveryShipResponse.estimatedMinutes());
        order.setVehicleType(deliveryShipResponse.vehicleType());

        BigDecimal totalPrice = order.getProductsPrice().add(order.getShippingPrice());
        order.setTotalPrice(totalPrice);

        Order saved = orderRepository.save(order);
        log.info("[ORDER] Pedido salvo - orderId={}, total={}", saved.getId(), saved.getTotalPrice());


        kafkaProducerService.sendPaymentApprovatedEvent(
                new PaymentApprovatedEvent(
                        saved.getId(), true
                )
        );

        log.info("[ORDER] Evento OrderCreated enviado - orderId={}", saved.getId());

        incrementarPedidosCriados();
        return saved;
    });
}



//    ✅ Metodo Kafka
    @Transactional
    public void updateStatusDelivery(Long orderId, String deliveryStatus){
        log.info("[ORDER] Atualizando status de entrega - orderId={}, status={}", orderId, deliveryStatus);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->{
                    log.warn("[ORDER] Pedido não encontrado - orderId={}", orderId);
                    return new OrderNotFoundException("Order not found");
                });

        DeliveryStatus status = DeliveryStatus.valueOf(deliveryStatus);
        order.setDeliveryStatus(status);
        log.info("[ORDER] Status de entrega atualizado - orderId={}, status={}", orderId, status);
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

    private void incrementarPedidosCriados(){
        orderMetrics.incrementPedidosCriados();
    }
}
