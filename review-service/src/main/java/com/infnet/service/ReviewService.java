package com.infnet.service;

import com.infnet.api.dto.CreateReplyRequest;
import com.infnet.api.dto.CreateReviewRequest;
import com.infnet.api.dto.StoreReviewSummaryResponse;
import com.infnet.api.exception.DuplicateReviewException;
import com.infnet.api.exception.ForbiddenAuthorizationException;
import com.infnet.api.exception.ReviewNotFoundException;
import com.infnet.domain.Review;
import com.infnet.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository repository;
    private final StoreService storeService;

    public Review create(CreateReviewRequest request, Long storeId, String role, Long authorId, String authorName) {
        if (!"CUSTOMER".equals(role)) {
            throw new ForbiddenAuthorizationException("Apenas clientes podem criar avaliações");
        }

        if (repository.existsByStoreIdAndAuthorId(storeId, authorId)) {
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

        return repository.save(review);
    }

    public Review reply(UUID reviewId, CreateReplyRequest request, String role, Long ownerId) {
        if (!"STORE_OWNER".equals(role)) {
            throw new ForbiddenAuthorizationException("Apenas lojistas podem responder avaliações");
        }

        Review review = repository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Avaliação não encontrada: " + reviewId));

        // TODO: quando store-service expor ownerId, validar:
        //   StoreResponseDTO store = storeClient.getStore(review.getStoreId());
        //   if (!Objects.equals(store.ownerId(), userId))
        //       throw new ForbiddenAuthorizationException("Você não é o dono desta loja");

        review.setOwnerReply(request.comment());
        review.setOwnerReplyAt(LocalDateTime.now());
        return repository.save(review);
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