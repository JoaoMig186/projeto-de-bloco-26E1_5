package com.infnet.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Geocode {
    private Double lat;
    private Double lon;

    public Geocode(Double lon, Double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    protected Geocode(){
    }
}
