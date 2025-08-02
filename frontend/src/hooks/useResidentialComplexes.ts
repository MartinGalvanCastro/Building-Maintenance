import { useQuery } from '@tanstack/react-query';
import { listResidentialComplexes } from '@/services/residentialComplexService';
import type { ResidentialComplexDto } from '@/api/api';

export function useResidentialComplexes() {
  return useQuery<ResidentialComplexDto[], Error>({
    queryKey: ['residentialComplexes'],
    queryFn: listResidentialComplexes,
  });
}
