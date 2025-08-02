package com.martin.buildingmaintenance.infrastructure.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.martin.buildingmaintenance.domain.model.MaintenanceRequest;
import com.martin.buildingmaintenance.infrastructure.mapper.MaintenanceRequestMapper;
import com.martin.buildingmaintenance.infrastructure.persistence.entity.MaintenanceRequestEntity;
import com.martin.buildingmaintenance.infrastructure.persistence.repository.JPAMaintenanceRequestRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaintenanceRequestPersistenceAdapterTest {
    @Mock private JPAMaintenanceRequestRepository jpa;
    @Mock private MaintenanceRequestMapper mapper;
    @InjectMocks private MaintenanceRequestPersistenceAdapter adapter;

    @Test
    void findByResidentId_returnsMappedList() {
        UUID residentId = UUID.randomUUID();
        MaintenanceRequestEntity entity = mock(MaintenanceRequestEntity.class);
        MaintenanceRequest domain = mock(MaintenanceRequest.class);
        when(jpa.findByResident_Id(residentId)).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        List<MaintenanceRequest> result = adapter.findByResidentId(residentId);
        assertEquals(1, result.size());
        assertSame(domain, result.get(0));
    }

    @Test
    void findByIdAndResidentId_returnsMappedOptional() {
        UUID requestId = UUID.randomUUID();
        UUID residentId = UUID.randomUUID();
        MaintenanceRequestEntity entity = mock(MaintenanceRequestEntity.class);
        MaintenanceRequest domain = mock(MaintenanceRequest.class);
        when(jpa.findByIdAndResident_Id(requestId, residentId)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        Optional<MaintenanceRequest> result = adapter.findByIdAndResidentId(requestId, residentId);
        assertTrue(result.isPresent());
        assertSame(domain, result.get());
    }

    @Test
    void findByAssignedTechnicianId_returnsMappedList() {
        UUID techId = UUID.randomUUID();
        MaintenanceRequestEntity entity = mock(MaintenanceRequestEntity.class);
        MaintenanceRequest domain = mock(MaintenanceRequest.class);
        when(jpa.findByTechnicianId(techId)).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        List<MaintenanceRequest> result = adapter.findByAssignedTechnicianId(techId);
        assertEquals(1, result.size());
        assertSame(domain, result.get(0));
    }

    @Test
    void findById_returnsMappedOptional() {
        UUID id = UUID.randomUUID();
        MaintenanceRequestEntity entity = mock(MaintenanceRequestEntity.class);
        MaintenanceRequest domain = mock(MaintenanceRequest.class);
        when(jpa.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        Optional<MaintenanceRequest> result = adapter.findById(id);
        assertTrue(result.isPresent());
        assertSame(domain, result.get());
    }

    @Test
    void deleteById_delegatesToJpa() {
        UUID id = UUID.randomUUID();
        adapter.deleteById(id);
        verify(jpa).deleteById(id);
    }

    @Test
    void save_mapsAndSavesRequest() {
        MaintenanceRequest domain = mock(MaintenanceRequest.class);
        MaintenanceRequestEntity entity = mock(MaintenanceRequestEntity.class);
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpa.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);
        MaintenanceRequest result = adapter.save(domain);
        assertSame(domain, result);
    }
}
