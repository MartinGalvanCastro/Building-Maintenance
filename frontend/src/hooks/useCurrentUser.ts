import { useQuery } from '@tanstack/react-query';
import { getCurrentUser } from '@/services/userInfoService';
import type { UserInfoDto } from '@/api/api';

export function useCurrentUser() {
  return useQuery<UserInfoDto>({
    queryKey: ['currentUser'],
    queryFn: getCurrentUser,
  });
}
