package com.infnet.model.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.infnet.model.enums.ProductEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode payload;

    private Boolean processed;

    protected OutboxProductEvent(){

    }

    public OutboxProductEvent(Long productId, ProductEventType eventType, JsonNode payload){
        this.productId = productId;
        this.eventType = eventType;
        this.payload = payload;
        processed = false;
    }
}
