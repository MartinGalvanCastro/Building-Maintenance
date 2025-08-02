import { TechnicianMaintenanceRequestsApi } from '../api/api';
import api, { apiBasePath } from '../utils/axios';
import type { MaintenanceRequestDto, ChangeStatusDto } from '../api/api';

const techReqApi = new TechnicianMaintenanceRequestsApi(undefined, apiBasePath, api);

export function listTechnicianRequests(technicianId: string): Promise<MaintenanceRequestDto[]> {
  return techReqApi.list1(technicianId).then(res => res.data);
}

export function changeTechnicianRequestStatus(
  technicianId: string,
  requestId: string,
  data: ChangeStatusDto
): Promise<MaintenanceRequestDto> {
  return techReqApi.changeStatus(technicianId, requestId, data).then(res => res.data);
}
