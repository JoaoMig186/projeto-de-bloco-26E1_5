package com.infnet.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class ReviewMetrics {
    private final Counter totalReviews;
    private final Counter totalReplies;

    public ReviewMetrics(MeterRegistry meterRegistry) {
        this.totalReviews = Counter.builder("icimento_review_service_reviews")
                .description("Total de avaliações criadas")
                .tag("service", "review-service")
                .register(meterRegistry);

        this.totalReplies = Counter.builder("icimento_review_service_replies")
                .description("Total de respostas de lojistas a avaliações")
                .tag("service", "review-service")
                .register(meterRegistry);
    }

    public void incrementTotalReviews() {
        totalReviews.increment();
    }

    public void incrementTotalReplies() {
        totalReplies.increment();
    }
}
