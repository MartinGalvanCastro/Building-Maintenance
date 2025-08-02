import { useMutation, useQueryClient } from '@tanstack/react-query';
import { deleteTechnician } from '@/services/technicianService';

export function useDeleteTechnician() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => deleteTechnician(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['technicians'] });
    },
  });
}
