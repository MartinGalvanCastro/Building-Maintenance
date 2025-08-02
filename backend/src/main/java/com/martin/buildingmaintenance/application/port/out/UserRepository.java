package com.martin.buildingmaintenance.application.port.out;

import com.martin.buildingmaintenance.domain.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);

    User save(User user);
}
