package com.infnet.delivery_sevice.repository.specification;

import com.infnet.delivery_sevice.domain.entity.Driver;
import org.springframework.data.jpa.domain.Specification;

public class DriverSpecification {

    public static Specification<Driver> byPartialName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Driver> byExactVehicleType(String vehicleType) {
        return (root, query, criteriaBuilder) -> {
            if (vehicleType == null || vehicleType.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("vehicleType")),
                    "%" + vehicleType.toLowerCase() + "%"
            );
        };
    }


}
