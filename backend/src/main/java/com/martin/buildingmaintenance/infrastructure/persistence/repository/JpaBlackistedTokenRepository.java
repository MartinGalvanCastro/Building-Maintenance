package com.martin.buildingmaintenance.infrastructure.persistence.repository;

import com.martin.buildingmaintenance.infrastructure.persistence.entity.BlacklistedTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBlackistedTokenRepository
        extends JpaRepository<BlacklistedTokenEntity, String> {}
