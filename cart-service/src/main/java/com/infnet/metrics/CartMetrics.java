package com.infnet.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CarrinhoMetrics {

    private final Counter carrinhosCriados;

    private final Counter carrinhosFinalizados;

    public CarrinhoMetrics(MeterRegistry registry) {

        this.carrinhosCriados =
                Counter.builder(
                                "icimento_cart_service_total_carts_created"
                        )
                        .description(
                                "Total de carrinhos criados"
                        )
                        .tag("service", "cart-service")
                        .register(registry);

        this.carrinhosFinalizados =
                Counter.builder(
                                "icimento_cart_service_total_carts_completed"
                        )
                        .description(
                                "Total de carrinhos finalizados"
                        )
                        .tag("service", "cart-service")
                        .register(registry);
    }

    public void incrementarCarrinhosCriados() {
        carrinhosCriados.increment();
    }

    public void incrementarCarrinhosFinalizados() {
        carrinhosFinalizados.increment();
    }
}