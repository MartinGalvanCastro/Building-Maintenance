import { AuthenticationApi } from '../api/api';
import api, { apiBasePath } from '../utils/axios';
import type { CredentialsDto, LogInResultDto } from '../api/api';

const authApi = new AuthenticationApi(undefined, apiBasePath, api);




export function login(credentials: CredentialsDto): Promise<LogInResultDto> {
  return authApi.login(credentials).then(res => res.data);
}

export function logout(): Promise<void> {
  return authApi.logout().then(() => undefined);
}

export function validateToken(token: string): Promise<void> {
  return authApi.validateToken(`Bearer ${token}`).then(() => undefined);
}
