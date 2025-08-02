import { useMutation, useQueryClient } from '@tanstack/react-query';
import { createTechnician, updateTechnician } from '@/services/technicianService';
import type { TechnicianFormValues } from '@/components/forms/TechnicianForm';
import type { TechnicianDto, TechnicianCreateCommandDtoSpecializationsEnum, TechnicianUpdateCommandDtoSpecializationsEnum } from '@/api/api';

export function useTechnicianMutation(technicianId?: string, onSuccess?: (data: TechnicianDto) => void) {
  const queryClient = useQueryClient();
  function technicianMutationFn(values: TechnicianFormValues): Promise<TechnicianDto> {
    if (technicianId) {
      return updateTechnician(technicianId, {
        fullName: values.fullName,
        email: values.email,
        password: values.password,
        specializations: values.specializations as TechnicianUpdateCommandDtoSpecializationsEnum[]
      });
    } else {
      return createTechnician({
        fullName: values.fullName,
        email: values.email,
        password: values.password ?? '',
        specializations: values.specializations as TechnicianCreateCommandDtoSpecializationsEnum[]
      });
    }
  }
  return useMutation({
    mutationFn: technicianMutationFn,
    onSuccess: data => {
      queryClient.invalidateQueries({ queryKey: ['technicians'] });
      onSuccess?.(data);
    },
  });
}
