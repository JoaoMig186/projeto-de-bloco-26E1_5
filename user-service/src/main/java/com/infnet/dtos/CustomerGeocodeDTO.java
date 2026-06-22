package com.infnet.dtos;

import com.infnet.model.CustomerProfile;
import com.infnet.model.User;

public record CustomerGeocodeDTO(
        Double lat,
        Double lon
) {
    public static CustomerGeocodeDTO fromDomain(CustomerProfile customer){
        return new CustomerGeocodeDTO(
                customer.getLat(),
                customer.getLon()
        );
    }
}
