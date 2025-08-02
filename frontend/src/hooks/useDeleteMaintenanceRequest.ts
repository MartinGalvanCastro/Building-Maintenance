import { useMutation, useQueryClient } from '@tanstack/react-query';
import { deleteMaintenanceRequestAdmin } from '@/services/maintenanceRequestService';

export function useDeleteMaintenanceRequest() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => deleteMaintenanceRequestAdmin(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['maintenanceRequests'] });
    },
  });
}
