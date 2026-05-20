package com.infnet.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Order {

    @Id
    private String id;
}
