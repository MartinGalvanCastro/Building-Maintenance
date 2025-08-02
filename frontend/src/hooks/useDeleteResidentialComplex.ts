import { useMutation, useQueryClient } from '@tanstack/react-query';
import { deleteResidentialComplex } from '@/services/residentialComplexService';

export function useDeleteResidentialComplex() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: string) => deleteResidentialComplex(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['residentialComplexes'] });
    },
  });
}
