package com.infnet.repository;

import com.infnet.model.Geocode;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Repository
public class GeocodeRepository {

    private final List<Geocode> userGeocodeList = new ArrayList<>();
    private final List<Geocode> storeGeocodeList = new ArrayList<>();

    private final Random random = new Random();

    public GeocodeRepository() {

        this.userGeocodeList.add(new Geocode(-22.9068, -43.1729));
        this.userGeocodeList.add(new Geocode(-23.5505, -46.6333));

        this.storeGeocodeList.add(new Geocode(-8.0476, -34.8770));
        this.storeGeocodeList.add(new Geocode(-3.7319, -38.5267));
    }

    public Geocode getUserGeocode(){
        return userGeocodeList.get(random.nextInt(userGeocodeList.size()));
    }

    public Geocode getStoreGeocode(){
        return storeGeocodeList.get(random.nextInt(storeGeocodeList.size()));
    }



}
