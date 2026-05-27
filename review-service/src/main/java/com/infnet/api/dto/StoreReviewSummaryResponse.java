package com.infnet.api.dto;

public record StoreReviewSummaryResponse(
        Double averageRating,
        Long totalReviews) {
}