package com.infnet.delivery_sevice.repository;

import com.infnet.delivery_sevice.domain.entity.Delivery;
import com.infnet.delivery_sevice.domain.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryRepository
        extends JpaRepository<Delivery, UUID>, JpaSpecificationExecutor<Driver> {
}
