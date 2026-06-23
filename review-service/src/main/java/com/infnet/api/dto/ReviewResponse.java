package com.infnet.api.dto;

import com.infnet.domain.Review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        String authorName,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        String storeReply,
        LocalDateTime storeReplyAt
) {
    public static ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getAuthorName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getStoreReply(),
                review.getStoreReplyAt()
        );
    }
}