package com.infnet.kafka;

import com.infnet.events.GeocodeEvent;
import com.infnet.metrics.UserMetrics;
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
    private final UserMetrics metrics;

    @KafkaListener(topics = "icimento.geocode.customer.scrape")
    public void receiveGeocodeEvent(GeocodeEvent event){

        System.out.println("EVENTO GEOCODE RECEBIDO!");
        CustomerProfile customer = repository.findById(event.userId()).get();

        customer.setLat(event.lat());
        customer.setLon(event.lon());
        customer.setStatus(Status.ACTIVE);

        metrics.decrementTotalPendingGeocodeCustomers();
        metrics.incrementTotalActiveCustomers();

        repository.save(customer);
    }


}
