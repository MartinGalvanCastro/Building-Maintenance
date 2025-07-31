package com.martin.buildingmaintenance.infrastructure.persistence.adapter;

import com.martin.buildingmaintenance.domain.model.ResidentialComplex;
import com.martin.buildingmaintenance.infrastructure.mapper.ResidentialComplexMapper;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.ResidentialComplexEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.repository.JPAResidentialComplexRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResidentialComplexPersistenceAdapterTest {
    @Mock
    private JPAResidentialComplexRepository jpa;
    @Mock
    private ResidentialComplexMapper mapper;
    @InjectMocks
    private ResidentialComplexPersistenceAdapter adapter;

    @Test
    void findAll_returnsMappedList() {
        ResidentialComplexEntity entity = mock(ResidentialComplexEntity.class);
        ResidentialComplex domain = mock(ResidentialComplex.class);
        when(jpa.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        List<ResidentialComplex> result = adapter.findAll();
        assertEquals(1, result.size());
        assertSame(domain, result.get(0));
    }

    @Test
    void findById_returnsMappedOptional() {
        UUID id = UUID.randomUUID();
        ResidentialComplexEntity entity = mock(ResidentialComplexEntity.class);
        ResidentialComplex domain = mock(ResidentialComplex.class);
        when(jpa.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        Optional<ResidentialComplex> result = adapter.findById(id);
        assertTrue(result.isPresent());
        assertSame(domain, result.get());
    }

    @Test
    void save_mapsAndSavesComplex() {
        ResidentialComplex domain = mock(ResidentialComplex.class);
        ResidentialComplexEntity entity = mock(ResidentialComplexEntity.class);
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);
        ResidentialComplex result = adapter.save(domain);
        assertSame(domain, result);
    }

    @Test
    void deleteById_delegatesToJpa() {
        UUID id = UUID.randomUUID();
        adapter.deleteById(id);
        verify(jpa).deleteById(id);
    }
}
