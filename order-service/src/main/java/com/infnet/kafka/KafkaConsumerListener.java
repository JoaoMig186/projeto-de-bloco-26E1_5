package com.infnet.kafka;

import com.infnet.kafka.events.DeliveryUpdatedEvent;
import com.infnet.kafka.events.PaymentUpdatedEvent;
import com.infnet.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumerListener {

    private final OrderService orderService;


    @KafkaListener(topics = "topicoX", groupId = "groupIdX")
    public void receberAtualizacaoDePagamento(PaymentUpdatedEvent event){
//        colocar log
//        orderService.updateStatusPayment(event.orderId(), event.paymentStatus());
        orderService.updateStatusPayment(2L,"APPROVED");
    }

    @KafkaListener(topics = "delivery-status-updated-topic", groupId = "delivery-group")
    public void receberAtualizacaoDeTransporte(DeliveryUpdatedEvent event){
        orderService.updateStatusDelivery(event.orderId(), event.deliveryStatus());
    }

}
