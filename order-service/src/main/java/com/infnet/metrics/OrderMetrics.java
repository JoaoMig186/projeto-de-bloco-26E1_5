package com.infnet.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class OrderMetrics {
    private final Counter pedidosCriados;
    private final Timer pedidoDuration;

    public OrderMetrics(MeterRegistry meterRegistry) {
        this.pedidosCriados = Counter.builder("icimento_order_pedidos_criados")
                .description("Todos pedidos criados")
                .tag("service", "order-icimento")
                .register( meterRegistry);

        this.pedidoDuration = Timer.builder("icimento_order_pedido_duration")
                .description("Tempo de duracao de pedido")
                .tag("service", "order-icimento")
                .publishPercentileHistogram()
                .register( meterRegistry);
    }

    public void incrementPedidosCriados() {
        pedidosCriados.increment();
    }

    public <T> T medirTempoDePedido(java.util.function.Supplier<T> operacao) {
        return pedidoDuration.record(operacao);
    }
}
