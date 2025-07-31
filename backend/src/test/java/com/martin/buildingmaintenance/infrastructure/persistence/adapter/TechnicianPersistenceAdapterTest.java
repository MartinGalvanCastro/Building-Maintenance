package com.martin.buildingmaintenance.infrastructure.persistence.adapter;

import com.martin.buildingmaintenance.domain.model.Specialization;
import com.martin.buildingmaintenance.domain.model.Technician;
import com.martin.buildingmaintenance.infrastructure.mapper.TechnicianMapper;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.TechnicianEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.repository.JPATechnicianRepository;
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
class TechnicianPersistenceAdapterTest {
    @Mock
    private JPATechnicianRepository jpa;
    @Mock
    private TechnicianMapper mapper;
    @InjectMocks
    private TechnicianPersistenceAdapter adapter;

    @Test
    void findAll_returnsMappedList() {
        TechnicianEntity entity = mock(TechnicianEntity.class);
        Technician domain = mock(Technician.class);
        when(jpa.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        List<Technician> result = adapter.findAll();
        assertEquals(1, result.size());
        assertSame(domain, result.get(0));
    }

    @Test
    void findById_returnsMappedOptional() {
        UUID id = UUID.randomUUID();
        TechnicianEntity entity = mock(TechnicianEntity.class);
        Technician domain = mock(Technician.class);
        when(jpa.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        Optional<Technician> result = adapter.findById(id);
        assertTrue(result.isPresent());
        assertSame(domain, result.get());
    }

    @Test
    void findBySpecialization_returnsMappedList() {
        Specialization specialization = mock(Specialization.class);
        TechnicianEntity entity = mock(TechnicianEntity.class);
        Technician domain = mock(Technician.class);
        when(jpa.findBySpecializationsContaining(specialization)).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        List<Technician> result = adapter.findBySpecialization(specialization);
        assertEquals(1, result.size());
        assertSame(domain, result.get(0));
    }

    @Test
    void save_mapsAndSavesTechnician() {
        Technician domain = mock(Technician.class);
        TechnicianEntity entity = mock(TechnicianEntity.class);
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);
        Technician result = adapter.save(domain);
        assertSame(domain, result);
    }

    @Test
    void deleteById_delegatesToJpa() {
        UUID id = UUID.randomUUID();
        adapter.deleteById(id);
        verify(jpa).deleteById(id);
    }
}
