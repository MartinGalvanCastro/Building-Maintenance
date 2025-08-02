import { useQuery } from '@tanstack/react-query';
import { listResidents } from '@/services/residentService';
import type { ResidentDto } from '@/api/api';

export function useResidents() {
  return useQuery<ResidentDto[], Error>({
    queryKey: ['residents'],
    queryFn: listResidents,
  });
}
