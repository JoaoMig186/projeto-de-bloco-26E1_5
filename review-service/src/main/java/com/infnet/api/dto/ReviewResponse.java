package com.infnet.api.dto;

import com.infnet.domain.Review;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        String authorName,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        String ownerReply,
        LocalDateTime ownerReplyAt
) {
    public static ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getAuthorName(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getOwnerReply(),
                review.getOwnerReplyAt()
        );
    }
}