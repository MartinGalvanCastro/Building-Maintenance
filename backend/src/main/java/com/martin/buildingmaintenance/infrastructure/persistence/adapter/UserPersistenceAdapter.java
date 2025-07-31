package com.martin.buildingmaintenance.infrastructure.persistence.adapter;

import com.martin.buildingmaintenance.application.port.out.UserRepository;
import com.martin.buildingmaintenance.domain.model.User;
import com.martin.buildingmaintenance.infrastructure.mapper.UserMapper;
import com.martin.buildingmaintenance.infrastructure.persistence.repository.JPAUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepository {
    private final JPAUserRepository jpa;
    private final UserMapper        mapper;


    @Override
    public Optional<User> findByEmail(String email) {
        return jpa.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public User save(User user) {
        var entity = mapper.toEntity(user);
        return mapper.toDomain(jpa.save(entity));
    }
}
