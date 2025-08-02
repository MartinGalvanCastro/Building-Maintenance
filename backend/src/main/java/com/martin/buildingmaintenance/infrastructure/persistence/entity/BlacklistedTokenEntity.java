package com.martin.buildingmaintenance.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "revoked_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class BlacklistedTokenEntity {

    @Id
    @Column(length = 512, nullable = false)
    private String token;

    @Column(name = "revoked_at", nullable = false)
    private Instant revokedAt;
}
