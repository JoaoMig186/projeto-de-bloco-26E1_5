package com.infnet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_reviews", schema="review_service")
@Getter@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "author_comment", length = 500)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "store_reply", length = 500)
    private String ownerReply;

    @Column(name = "reply_at")
    private LocalDateTime ownerReplyAt;

    protected Review() {
    }

    public Review(Long storeId, Long authorId, String authorName,
                  Integer rating, String comment, LocalDateTime createdAt) {
        this.storeId = storeId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}