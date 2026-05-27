package com.infnet.service;

import com.infnet.api.dto.CreateReviewRequest;
import com.infnet.api.dto.StoreReviewSummaryResponse;
import com.infnet.domain.Review;
import com.infnet.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository repository;

    public Review create(CreateReviewRequest request, Long storeId, Long authorId, String authorName) {
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

    public List<Review> findByStore(Long storeId) {
        return repository.findByStoreId(storeId);
    }

    public StoreReviewSummaryResponse getSummary(Long storeId) {
        Double average = repository.findAverageRatingByStoreId(storeId).orElse(0.0);
        Long total = repository.countByStoreId(storeId);

        return new StoreReviewSummaryResponse(average, total);
    }
}