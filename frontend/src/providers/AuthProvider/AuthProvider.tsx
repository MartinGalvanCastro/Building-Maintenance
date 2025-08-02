import { useState, useEffect, useMemo } from 'react';
import { decodeJwt } from '@/utils/jwt';
import type { JwtPayload } from '@/utils/jwt';
import type { CredentialsDto, LogInResultDto } from '@/api/api';
import { login as loginService, logout as logoutService, validateToken } from '@/services';
import { AuthContext } from './AuthContext';
import { useMutation } from '@tanstack/react-query';

export function AuthProvider({ children }: { children: React.ReactNode }) {

  const [token, setToken] = useState<string | null>(null);
  const [isTokenValid, setIsTokenValid] = useState<boolean>(false);
  const [tokenPayload, setTokenPayload] = useState<JwtPayload | null>(null);

  const {
    mutateAsync: loginMutation,
    status: loginStatus,
    error: loginErrorRaw,
    reset: resetLoginError
  } = useMutation<LogInResultDto, unknown, CredentialsDto>({
    mutationFn: async (credentials: CredentialsDto) => loginService(credentials),
    onSuccess: (result) => {
      if (result.token) {
        setToken(result.token);
        setIsTokenValid(true);
        setTokenPayload(decodeJwt(result.token));
        localStorage.setItem('token', result.token);
      }
    },
  });

  const {
    mutateAsync: logoutMutation,
    status: logoutStatus,
    error: logoutErrorRaw,
    reset: resetLogoutError
  } = useMutation<void, unknown, void>({
    mutationFn: async () => logoutService(),
    onSuccess: () => {
      setToken(null);
      setIsTokenValid(false);
      setTokenPayload(null);
      resetValidateError();
      localStorage.removeItem('token');
    },
  });

  const {
    mutateAsync: validateTokenMutation,
    reset: resetValidateError
  } = useMutation<void, unknown, string>({
    mutationFn: async (token: string) => validateToken(token),
    onSuccess: () => setIsTokenValid(true),
    onError: () => setIsTokenValid(false),
  });

  useEffect(() => {
    const storedToken = localStorage.getItem('token');
    if (token) {
      validateTokenMutation(token);
      setTokenPayload(decodeJwt(token));
    } else if (storedToken) {
      setToken(storedToken);
      validateTokenMutation(storedToken);
      setTokenPayload(decodeJwt(storedToken));
    } else {
      setIsTokenValid(false);
      setTokenPayload(null);
    }
  }, [token, validateTokenMutation]);




  const contextValue = useMemo(() => ({
    isAuthenticated: !!token && isTokenValid,
    token,
    tokenPayload,
    login: loginMutation,
    logout: logoutMutation,
    isLoggingIn: loginStatus === 'pending',
    isLoggingOut: logoutStatus === 'pending',
    loginError: !!loginErrorRaw,
    logoutError: !!logoutErrorRaw,
    resetLoginError,
    resetLogoutError,
  }), [token, isTokenValid, tokenPayload, loginMutation, logoutMutation, loginStatus, logoutStatus, loginErrorRaw, logoutErrorRaw, resetLoginError, resetLogoutError]);

  return (
    <AuthContext.Provider value={contextValue}>
      {children}
    </AuthContext.Provider>
  );
}

