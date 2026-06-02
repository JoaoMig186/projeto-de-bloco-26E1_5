package com.infnet.kafka;

import com.infnet.events.GeocodeEvent;
import com.infnet.model.CustomerProfile;
import com.infnet.model.enums.Status;
import com.infnet.repository.CustomerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("prod")
public class KafkaCustomerListener {

    private final CustomerProfileRepository repository;

    @KafkaListener(topics = "microservices.geocode.customer.scrape.success")
    public void receiveGeocodeEvent(GeocodeEvent event){

        System.out.println("EVENTO GEOCODE RECEBIDO!");
        CustomerProfile customer = repository.findById(event.userId()).get();

        customer.setLat(event.lat());
        customer.setLon(event.lon());
        customer.setStatus(Status.ACTIVE);

        repository.save(customer);
    }


}
