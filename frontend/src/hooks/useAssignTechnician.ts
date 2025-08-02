
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { assignTechnicianToRequest } from '@/services/maintenanceRequestService';


export function useAssignTechnician(requestId: string, onSuccess: () => void) {
  const queryClient = useQueryClient();
  const mutation = useMutation({
    mutationFn: (technicianId: string) => assignTechnicianToRequest(requestId, technicianId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['currentUser'] });
      onSuccess();
    },
  });
  return {
    mutate: mutation.mutate,
    isPending: mutation.isPending,
  };
}
