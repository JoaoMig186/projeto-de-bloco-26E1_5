package com.infnet.service;

import com.infnet.api.dto.CreateReplyRequest;
import com.infnet.api.dto.CreateReviewRequest;
import com.infnet.api.dto.StoreReviewSummaryResponse;
import com.infnet.api.dto.ValidacaoStoreResponse;
import com.infnet.api.exception.DuplicateReviewException;
import com.infnet.api.exception.ForbiddenAuthorizationException;
import com.infnet.api.exception.ReviewNotFoundException;
import com.infnet.domain.Review;
import com.infnet.kafka.KafkaService;
import com.infnet.metrics.ReviewMetrics;
import com.infnet.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository repository;
    private final StoreService storeService;
    private final KafkaService kafkaService;
    private final ReviewMetrics metrics;
    private final Logger log = LoggerFactory.getLogger(ReviewService.class);

    public Review create(CreateReviewRequest request, Long storeId, String role, Long authorId, String authorName) {
        if (!"CUSTOMER".equals(role)) {
            log.warn("Usuário {} ({}) tentou criar review sem permissão", authorId, role);
            throw new ForbiddenAuthorizationException("Apenas clientes podem criar avaliações");
        }

        if (repository.existsByStoreIdAndAuthorId(storeId, authorId)) {
            log.warn("Usuário {} tentou avaliar loja {} mais de uma vez", authorId, storeId);
            throw new DuplicateReviewException("Você já avaliou esta loja");
        }

        storeService.validarStore(storeId);


        Review review = new Review(
                storeId,
                authorId,
                authorName,
                request.rating(),
                request.comment(),
                LocalDateTime.now()
        );

        Review saved = repository.save(review);
        log.info("Review criada: id={}, storeId={}, authorId={}", saved.getId(), storeId, authorId);
        metrics.incrementTotalReviews();

        StoreReviewSummaryResponse summary = getSummary(storeId);
        kafkaService.sendStoreRatingUpdatedEvent(storeId, summary.averageRating(), summary.totalReviews());
        log.info("Evento StoreRatingUpdated publicado: storeId={}, média={}, total={}",
                storeId, summary.averageRating(), summary.totalReviews());

        return saved;
    }

    public Review reply(UUID reviewId, CreateReplyRequest request, String role, Long ownerId) {
        if (!"STORE_OWNER".equals(role)) {
            log.warn("Usuário {} ({}) tentou responder review sem permissão", ownerId, role);
            throw new ForbiddenAuthorizationException("Apenas lojistas podem responder avaliações");
        }

        Review review = repository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Avaliação não encontrada: " + reviewId));

        ValidacaoStoreResponse store = storeService.validarStore(review.getStoreId());

        if (!store.ownerId().equals(ownerId)) {
            throw new ForbiddenAuthorizationException("Usuário não é proprietário da loja");
        }

        review.setOwnerReply(request.comment());
        review.setOwnerReplyAt(LocalDateTime.now());
        Review saved = repository.save(review);
        metrics.incrementTotalReplies();

        return saved;
    }

    public List<Review> findByStore(Long storeId) {
        return repository.findByStoreId(storeId);
    }

    public StoreReviewSummaryResponse getSummary(Long storeId) {
        Double average = repository.findAverageRatingByStoreId(storeId).orElse(0.0);
        Long total = repository.countByStoreId(storeId);

        return new StoreReviewSummaryResponse(average, total);
    }
}