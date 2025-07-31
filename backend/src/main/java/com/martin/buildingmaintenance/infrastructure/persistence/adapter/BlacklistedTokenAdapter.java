package com.martin.buildingmaintenance.infrastructure.persistence.adapter;

import com.martin.buildingmaintenance.application.port.out.TokenBlacklistRepository;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.BlacklistedTokenEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.repository.JpaBlackistedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
@RequiredArgsConstructor
public class BlacklistedTokenAdapter implements TokenBlacklistRepository {

    private final JpaBlackistedTokenRepository tokenBlacklistRepository;

    @Override
    public void revoke(String token) {
        tokenBlacklistRepository.save(
                BlacklistedTokenEntity.builder()
                        .token(token)
                        .revokedAt(Instant.now())
                        .build()
        );
    }

    @Override
    public boolean isRevoked(String token) {
        return tokenBlacklistRepository.existsById(token);
    }
}
