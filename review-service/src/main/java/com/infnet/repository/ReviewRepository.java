package com.infnet.repository;

import com.infnet.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByStoreId(Long storeId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.storeId = :storeId")
    Optional<Double> findAverageRatingByStoreId(@Param("storeId") Long storeId);

    Long countByStoreId(Long storeId);
}