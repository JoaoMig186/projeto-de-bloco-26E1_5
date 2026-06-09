package com.infnet.api.controller;

import com.infnet.api.dto.CreateReviewRequest;
import com.infnet.api.dto.ReviewResponse;
import com.infnet.api.dto.StoreReviewSummaryResponse;
import com.infnet.domain.Review;
import com.infnet.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;

    @PostMapping("/store/{storeId}")
    public ResponseEntity<ReviewResponse> create(
            @PathVariable("storeId") Long storeId,
            @RequestBody @Valid CreateReviewRequest request) {
        Review review = service.create(request, storeId, 1L, "João Pedro");
        return ResponseEntity.status(HttpStatus.CREATED).body(ReviewResponse.toResponse(review));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ReviewResponse>> findByStore(@PathVariable("storeId") Long storeId) {
        List<ReviewResponse> reviews = service.findByStore(storeId)
                .stream()
                .map(ReviewResponse::toResponse)
                .toList();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/store/{storeId}/summary")
    public ResponseEntity<StoreReviewSummaryResponse> getSummary(@PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(service.getSummary(storeId));
    }
}