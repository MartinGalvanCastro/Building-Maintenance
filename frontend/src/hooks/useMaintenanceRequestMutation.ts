
import { useMutation, useQueryClient } from '@tanstack/react-query';
import {
  updateMaintenanceRequestAdmin,
  createMaintenanceRequestAdmin,
  updateMaintenanceRequestResident,
  createMaintenanceRequestResident,
  changeMaintenanceRequestStatusTechnician,
} from '@/services/maintenanceRequestService';
import type { MaintenanceRequestDto, UpdateRequestDto, CreateRequestDto, CreateMaintenanceRequestDto, ChangeStatusDto } from '@/api/api';




interface UseMaintenanceRequestMutationProps {
  role: 'ADMIN' | 'RESIDENT' | 'TECHNICIAN';
  requestId?: string;
  residentId?: string;
  technicianId?: string;
  onSuccess?: (data: MaintenanceRequestDto) => void;
}


export function useMaintenanceRequestMutation({
  role,
  requestId,
  residentId,
  technicianId,
  onSuccess,
}: UseMaintenanceRequestMutationProps) {
  const queryClient = useQueryClient();
  function requestMutationFn(values: UpdateRequestDto | CreateRequestDto | CreateMaintenanceRequestDto | ChangeStatusDto): Promise<MaintenanceRequestDto> {
    if (role === 'ADMIN') {
      if (requestId) {
        return updateMaintenanceRequestAdmin(requestId, values as UpdateRequestDto);
      } else {
        return createMaintenanceRequestAdmin(values as CreateMaintenanceRequestDto);
      }
    } else if (role === 'RESIDENT' && residentId) {
      if (requestId) {
        return updateMaintenanceRequestResident(residentId, requestId, values as UpdateRequestDto);
      } else {
        return createMaintenanceRequestResident(residentId, values as CreateRequestDto);
      }
    } else if (role === 'TECHNICIAN' && technicianId && requestId) {
      return changeMaintenanceRequestStatusTechnician(technicianId, requestId, values as ChangeStatusDto);
    }
    return Promise.reject(new Error('Invalid mutation parameters'));
  }
  return useMutation({
    mutationFn: requestMutationFn,
    onSuccess: data => {
      queryClient.invalidateQueries({ queryKey: ['currentUser'] });
      onSuccess?.(data);
    },
  });
}
