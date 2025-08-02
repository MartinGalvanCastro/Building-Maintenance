import { useMutation, useQueryClient } from '@tanstack/react-query';
import { createResidentialComplex, updateResidentialComplex } from '@/services/residentialComplexService';
import type { ResidentialComplexCommandDto, ResidentialComplexDto } from '@/api/api';

export function useResidentialComplexMutation(complexId?: string, onSuccess?: (data: ResidentialComplexDto) => void) {
  const queryClient = useQueryClient();
  function complexMutationFn(values: ResidentialComplexCommandDto): Promise<ResidentialComplexDto> {
    if (complexId) {
      return updateResidentialComplex(complexId, values);
    } else {
      return createResidentialComplex(values);
    }
  }
  return useMutation({
    mutationFn: complexMutationFn,
    onSuccess: data => {
      queryClient.invalidateQueries({ queryKey: ['residentialComplexes'] });
      onSuccess?.(data);
    },
  });
}
