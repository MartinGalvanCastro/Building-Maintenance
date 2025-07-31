package com.martin.buildingmaintenance.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "revoked_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BlacklistedTokenEntity {

    @Id
    @Column(length = 512, nullable = false)
    private String token;

    @Column(name = "revoked_at", nullable = false)
    private Instant revokedAt;
}