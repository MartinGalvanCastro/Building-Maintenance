import { useMutation, useQueryClient } from '@tanstack/react-query';
import { createResident, updateResident } from '@/services/residentService';
import type { ResidentCreateCommandDto, ResidentUpdateCommandDto, ResidentDto } from '@/api/api';

export function useResidentMutation(residentId?: string, onSuccess?: (data: ResidentDto) => void) {
  const queryClient = useQueryClient();
  function residentMutationFn(values: ResidentCreateCommandDto | ResidentUpdateCommandDto): Promise<ResidentDto> {
    if (residentId) {
      return updateResident(residentId, values as ResidentUpdateCommandDto);
    } else {
      return createResident(values as ResidentCreateCommandDto);
    }
  }
  return useMutation({
    mutationFn: residentMutationFn,
    onSuccess: data => {
      onSuccess?.(data);
    },
    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['residents'] });
    },
  });
}
