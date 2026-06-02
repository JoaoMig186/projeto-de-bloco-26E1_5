package com.infnet.model;

import com.infnet.model.enums.Role;
import com.infnet.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_customer_profiles",schema = "user_service")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class CustomerProfile {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @MapsId
    private User user;

    private String address;

    private Double lat;

    private Double lon;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    protected CustomerProfile() {
    }

    public CustomerProfile(User user, String address) {
        this.user = user;
        this.address = address;
        this.status = Status.PENDING_GEOCODE;
    }
}