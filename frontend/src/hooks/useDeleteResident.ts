import { useMutation, useQueryClient } from '@tanstack/react-query';
import { deleteResident } from '@/services/residentService';

export function useDeleteResident() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => deleteResident(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['residents'] });
    },
  });
}
