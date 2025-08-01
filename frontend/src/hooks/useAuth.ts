import { useContext } from 'react';

import { AuthContext } from '../providers/AuthProvider';
import type { AuthContextType } from '../providers/AuthProvider';


export function useAuth(): AuthContextType   {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
