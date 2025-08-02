import { useMutation, useQueryClient } from '@tanstack/react-query';
import { deleteMaintenanceRequestResident } from '@/services/maintenanceRequestService';
import type { CancelResponseDto } from '@/api/api';

interface UseMaintenanceRequestCancelProps {
  residentId: string;
  onSuccess?: (data: CancelResponseDto) => void;
}

export function useMaintenanceRequestCancel({ residentId, onSuccess }: UseMaintenanceRequestCancelProps) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (requestId: string) => deleteMaintenanceRequestResident(residentId, requestId),
    onSuccess: data => {
      queryClient.invalidateQueries({ queryKey: ['currentUser'] });
      onSuccess?.(data);
    },
  });
}
