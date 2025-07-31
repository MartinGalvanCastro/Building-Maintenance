package com.martin.buildingmaintenance.application.port.out;

public interface TokenBlacklistRepository {

    void revoke(String token);

    boolean isRevoked(String token);
}
