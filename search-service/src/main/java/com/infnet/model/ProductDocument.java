package com.infnet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

@Document(indexName = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocument {

    @Id
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Long storeId;

    private String storeName;

    private String category;

    private String durability;
}