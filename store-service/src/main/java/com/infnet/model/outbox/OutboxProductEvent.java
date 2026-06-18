package com.infnet.model.outbox;

import com.infnet.model.enums.ProductEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;


@Data
@AllArgsConstructor
@Entity
@Table(name = "tb_product_events", schema = "store_service")
public class OutboxProductEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long productId;

    @Enumerated(EnumType.STRING)
    private ProductEventType eventType;

    @Column(columnDefinition = "jsonb")
    private String payload;

    private Boolean processed;

    protected OutboxProductEvent(){

    }

    public OutboxProductEvent(Long productId, ProductEventType eventType, String payload){
        this.productId = productId;
        this.eventType = eventType;
        this.payload = payload;
    }
}
