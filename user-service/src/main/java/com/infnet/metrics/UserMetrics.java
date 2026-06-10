package com.infnet.metrics;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class UserMetrics {
    private final Counter totalUsers;
    private final Counter totalCustomers;
    private final Counter totalStoreOwners;

    private final AtomicInteger totalInactiveCustomers = new AtomicInteger(0);
    private final AtomicInteger totalActiveCustomers = new AtomicInteger(0);
    private final AtomicInteger totalPendingGeocodeCustomers = new AtomicInteger(0);
    private final AtomicInteger totalInactiveStoreOwners = new AtomicInteger(0);
    private final AtomicInteger totalActiveStoreOwners = new AtomicInteger(0);

    public UserMetrics(MeterRegistry meterRegistry){
        this.totalUsers = Counter.builder("icimento_user-service_total_users")
                .description("Total de Usuários no Sistema")
                .tag("service","user-service")
                .register(meterRegistry);

        this.totalCustomers = Counter.builder("icimento_user-service_total_customers")
                .description("Total de Clientes no Sistema")
                .tag("service","user-service")
                .register(meterRegistry);

        this.totalStoreOwners = Counter.builder("icimento_user_service_total_store_owners")
                .description("Total de Lojas no Sistema")
                .tag("service","user-service")
                .register(meterRegistry);

        Gauge.builder(
                        "icimento_user_service_total_inactive_customers",
                        totalInactiveCustomers,
                        AtomicInteger::get
                ).description("Total de Clientes Inativos")
                .register(meterRegistry);

        Gauge.builder(
                        "icimento_user_service_total_active_customers",
                        totalActiveCustomers,
                        AtomicInteger::get
                ).description("Total de Clientes Ativos")
                .register(meterRegistry);

        Gauge.builder(
                        "icimento_user_service_total_pending_geocode_customers",
                        totalPendingGeocodeCustomers,
                        AtomicInteger::get
                ).description("Total de Clientes Pendentes")
                .register(meterRegistry);

        Gauge.builder(
                        "icimento_user_service_total_inactive_store_owners",
                        totalInactiveStoreOwners,
                        AtomicInteger::get
                ).description("Total de Lojas Inativas")
                .register(meterRegistry);

        Gauge.builder(
                        "icimento_user_service_total_active_store_owners",
                        totalActiveStoreOwners,
                        AtomicInteger::get
                ).description("Total de Lojas Ativas")
                .register(meterRegistry);
    }

    public void incrementTotalUsers(){
        totalUsers.increment();
    }

    public void incrementTotalCustomers(){
        totalCustomers.increment();
    }

    public void incrementTotalStoreOwners(){
        totalStoreOwners.increment();
    }

    public void incrementTotalActiveCustomers() {
        totalActiveCustomers.incrementAndGet();
    }

    public void decrementTotalActiveCustomers() {
        totalActiveCustomers.decrementAndGet();
    }

    public void incrementTotalInactiveCustomers() {
        totalInactiveCustomers.incrementAndGet();
    }

    public void decrementTotalInactiveCustomers() {
        totalInactiveCustomers.decrementAndGet();
    }

    public void incrementTotalPendingGeocodeCustomers() {
        totalPendingGeocodeCustomers.incrementAndGet();
    }

    public void decrementTotalPendingGeocodeCustomers() {
        totalPendingGeocodeCustomers.decrementAndGet();
    }

    public void incrementTotalActiveStoreOwners() {
        totalActiveStoreOwners.incrementAndGet();
    }

    public void decrementTotalActiveStoreOwners() {
        totalActiveStoreOwners.decrementAndGet();
    }

    public void incrementTotalInactiveStoreOwners() {
        totalInactiveStoreOwners.incrementAndGet();
    }

    public void decrementTotalInactiveStoreOwners() {
        totalInactiveStoreOwners.decrementAndGet();
    }

}
