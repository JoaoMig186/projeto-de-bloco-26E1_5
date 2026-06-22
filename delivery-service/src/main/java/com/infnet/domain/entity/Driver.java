package com.infnet.domain.entity;

import com.infnet.domain.entity.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String phone;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private Double latitude;

    private Double longitude;

    private Boolean available;

    public static Driver create(
            String name,
            String phone,
            VehicleType vehicleType
    ){
        Driver driver = new Driver();
        driver.name = name;
        driver.phone = phone;
        driver.vehicleType = vehicleType;
        driver.available = true;

        return driver;
    };

    public void update(
            String name,
            String phone,
            VehicleType vehicleType
    ) {
        this.name = name;
        this.phone = phone;
        this.vehicleType = vehicleType;
    }

    public void becomeAvailable() {
        this.available = true;
    }

    public void becomeUnavailable() {
        this.available = false;
    }

    public void updateLocation(
            Double latitude,
            Double longitude
    ) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
