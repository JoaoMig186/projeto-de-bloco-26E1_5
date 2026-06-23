package com.infnet.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SearchMetrics {
    private final Counter totalSearchs;

    public SearchMetrics(MeterRegistry meterRegistry){
        this.totalSearchs = Counter.builder("icimento_search_service_total_searchs")
                .description("Total de Pesquisas Realizadas no Sistema")
                .tag("service","search-service")
                .register(meterRegistry);
    }

    public void incrementTotalSearchs(){
        totalSearchs.increment();
    }

}
