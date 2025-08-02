import { useQuery } from '@tanstack/react-query';
import { listTechnicians } from '@/services/technicianService';
import type { TechnicianDto } from '@/api/api';

export function useTechnicians() {
  return useQuery<TechnicianDto[], Error>({
    queryKey: ['technicians'],
    queryFn: listTechnicians,
  });
}
