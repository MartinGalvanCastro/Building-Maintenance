package com.martin.buildingmaintenance.infrastructure.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.martin.buildingmaintenance.domain.model.Resident;
import com.martin.buildingmaintenance.infrastructure.mapper.ResidentMapper;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.ResidentEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.repository.JPAResidentRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResidentPersistenceAdapterTest {
    @Mock private JPAResidentRepository jpa;
    @Mock private ResidentMapper mapper;
    @InjectMocks private ResidentPersistenceAdapter adapter;

    @Test
    void findAll_returnsMappedList() {
        ResidentEntity entity = mock(ResidentEntity.class);
        Resident domain = mock(Resident.class);
        when(jpa.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        List<Resident> result = adapter.findAll();
        assertEquals(1, result.size());
        assertSame(domain, result.get(0));
    }

    @Test
    void findById_returnsMappedOptional() {
        UUID id = UUID.randomUUID();
        ResidentEntity entity = mock(ResidentEntity.class);
        Resident domain = mock(Resident.class);
        when(jpa.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        Optional<Resident> result = adapter.findById(id);
        assertTrue(result.isPresent());
        assertSame(domain, result.get());
    }

    @Test
    void save_mapsAndSavesResident() {
        Resident domain = mock(Resident.class);
        ResidentEntity entity = mock(ResidentEntity.class);
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);
        Resident result = adapter.save(domain);
        assertSame(domain, result);
    }

    @Test
    void deleteById_delegatesToJpa() {
        UUID id = UUID.randomUUID();
        adapter.deleteById(id);
        verify(jpa).deleteById(id);
    }
}
