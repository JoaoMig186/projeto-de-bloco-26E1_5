package com.infnet.service;

import com.infnet.model.Geocode;
import com.infnet.repository.GeocodeRepository;
import org.springframework.stereotype.Service;

@Service
public class GeocodeService {

    private final GeocodeRepository repository;

    public GeocodeService(GeocodeRepository repository){
        this.repository = repository;
    }

    public Geocode getUserGeocode(String address){
        return repository.getUserGeocode();
    }

    public Geocode getStoreGeocode(String address){
        return repository.getStoreGeocode();
    }



}
