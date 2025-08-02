
import { createContext } from 'react';
import type { CredentialsDto } from '../../api/api';

import type { UseMutateAsyncFunction } from '@tanstack/react-query';
import type { LogInResultDto } from '../../api/api';

import type { JwtPayload } from '@/utils/jwt';

export interface AuthContextType {
  isAuthenticated: boolean;
  token: string | null;
  tokenPayload: JwtPayload | null;
  login: UseMutateAsyncFunction<LogInResultDto, unknown, CredentialsDto, unknown>;
  logout: UseMutateAsyncFunction<void, unknown, void, unknown>;
  isLoggingIn: boolean;
  isLoggingOut: boolean;
  loginError: boolean;
  logoutError: boolean;
  resetLoginError: () => void;
  resetLogoutError: () => void;
}


export const AuthContext = createContext<AuthContextType | undefined>(undefined);


