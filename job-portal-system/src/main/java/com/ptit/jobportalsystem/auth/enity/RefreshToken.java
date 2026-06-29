package com.ptit.jobportalsystem.auth.enity;

import com.ptit.jobportalsystem.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String token; // UUID random


    @Column(name = "expiry_date", nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean revoked = false; // logout → set true

//    @CreationTimestamp
//    private LocalDateTime createdAt;
}
