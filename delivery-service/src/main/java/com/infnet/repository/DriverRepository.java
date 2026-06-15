package com.infnet.repository;

import com.infnet.domain.entity.Driver;
import com.infnet.domain.entity.enums.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverRepository
        extends JpaRepository<Driver, UUID>, JpaSpecificationExecutor<Driver> {

    Page<Driver> findByAvailableTrue(Pageable pageable);

    Page<Driver> findByAvailableTrueAndVehicleType(VehicleType vehicleType, Pageable pageable);

    Optional<Driver> findFirstByAvailableTrueAndVehicleType(VehicleType vehicleType);
}
