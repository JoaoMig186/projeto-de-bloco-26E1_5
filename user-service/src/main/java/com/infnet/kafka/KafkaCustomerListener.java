package com.infnet.kafka;

import com.infnet.events.GeocodeEvent;
import com.infnet.metrics.UserMetrics;
import com.infnet.model.CustomerProfile;
import com.infnet.model.enums.Status;
import com.infnet.repository.CustomerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("prod")
public class KafkaCustomerListener {

    private final CustomerProfileRepository repository;
    private final UserMetrics metrics;
    private static final Logger log = LoggerFactory.getLogger(KafkaCustomerListener.class);

    @KafkaListener(topics = "icimento.geocode.customer.scrape")
    public void receiveGeocodeEvent(GeocodeEvent event){

        log.info("Evento Geocode do Cliente Recebido com Sucesso");
        CustomerProfile customer = repository.findById(event.userId()).get();

        customer.setLat(event.lat());
        customer.setLon(event.lon());
        customer.setStatus(Status.ACTIVE);

        log.info("Cliente " + customer.getUser().getEmail() + " ativado");
        metrics.decrementTotalPendingGeocodeCustomers();
        metrics.incrementTotalActiveCustomers();

        repository.save(customer);
    }


}
