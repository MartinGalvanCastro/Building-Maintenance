import { MaintenanceRequestAdminScreen } from './MaintenanceRequestAdminScreen';
import { MaintenanceRequestResidentScreen } from './MaintenanceRequestResidentScreen';
import { MaintenanceRequestTechnicianScreen } from './MaintenanceRequestTechnicianScreen';
import { useAuth } from '@/hooks';

export function MaintenanceRequestScreen() {
  const { tokenPayload } = useAuth();
  if (tokenPayload?.role === 'ADMIN') return <MaintenanceRequestAdminScreen />;
  if (tokenPayload?.role === 'RESIDENT') return <MaintenanceRequestResidentScreen />;
  if (tokenPayload?.role === 'TECHNICIAN') return <MaintenanceRequestTechnicianScreen />;
  return null;
}
