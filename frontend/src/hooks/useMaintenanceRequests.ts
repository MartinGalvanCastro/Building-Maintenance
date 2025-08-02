import { useQuery } from '@tanstack/react-query';
import { listMaintenanceRequestsAdmin } from '@/services/maintenanceRequestService';
import type { MaintenanceRequestDto } from '@/api/api';

export function useMaintenanceRequests() {
  return useQuery<MaintenanceRequestDto[], Error>({
    queryKey: ['maintenanceRequests'],
    queryFn: listMaintenanceRequestsAdmin
  });
}
