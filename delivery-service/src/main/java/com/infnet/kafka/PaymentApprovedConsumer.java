package com.infnet.kafka;

import com.infnet.events.PaymentApprovatedEvent;
import com.infnet.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentApprovedConsumer {

    private final DeliveryService deliveryService;

    @KafkaListener(
            topics = "payment-approvated-topic",
            groupId = "delivery-group"
    )
    public void consume(PaymentApprovatedEvent event) {

        log.info(
                "Evento de pagamento recebido. Pedido={}, aprovado={}",
                event.orderId(),
                event.paymentApproved()
        );

        if (Boolean.TRUE.equals(event.paymentApproved())) {

            deliveryService.startDeliveryFromPayment(event.orderId());
        }
    }
}