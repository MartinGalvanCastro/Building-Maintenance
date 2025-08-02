package com.martin.buildingmaintenance.infrastructure.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.martin.buildingmaintenance.domain.model.User;
import com.martin.buildingmaintenance.infrastructure.mapper.UserMapper;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.UserEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.repository.JPAUserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest {
    @Mock private JPAUserRepository jpa;
    @Mock private UserMapper mapper;
    @InjectMocks private UserPersistenceAdapter adapter;

    @Test
    void findByEmail_returnsMappedOptional() {
        String email = "test@example.com";
        UserEntity entity = mock(UserEntity.class);
        User domain = mock(User.class);
        when(jpa.findByEmail(email)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        Optional<User> result = adapter.findByEmail(email);
        assertTrue(result.isPresent());
        assertSame(domain, result.get());
    }

    @Test
    void save_mapsAndSavesUser() {
        User domain = mock(User.class);
        UserEntity entity = mock(UserEntity.class);
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);
        User result = adapter.save(domain);
        assertSame(domain, result);
    }
}
