import { Routes, Route, Navigate } from 'react-router-dom';
import { LoginScreen, HomeScreen, ResidentialComplexScreen, TechniciansScreen, ResidentsScreen, MaintenanceRequestScreen } from '@/screens';

import { useAuth } from '@/hooks';


export function AppRouter() {
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return (
      <Routes>
        <Route path="/login" element={<LoginScreen />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    );
  }

  return (
    <Routes>
      <Route path="/login" element={<Navigate to="/home" replace />} />
      <Route path="/home" element={<HomeScreen />} />
      <Route path="/residential-complex" element={<ResidentialComplexScreen />} />
      <Route path="/technicians" element={<TechniciansScreen />} />
      <Route path="/residents" element={<ResidentsScreen />} />
      <Route path="/maintenance-request" element={<MaintenanceRequestScreen />} />
      <Route path="*" element={<Navigate to="/home" replace />} />
    </Routes>
  );
}
