import { ResidentMaintenanceRequestsApi } from '../api/api';
import api, { apiBasePath } from '../utils/axios';
import type {
  MaintenanceRequestDto,
  CreateRequestDto,
  UpdateRequestDto,
  CancelResponseDto
} from '../api/api';

const residentReqApi = new ResidentMaintenanceRequestsApi(undefined, apiBasePath, api);

export function listResidentRequests(residentId: string): Promise<MaintenanceRequestDto[]> {
  return residentReqApi.list(residentId).then(res => res.data);
}

export function getResidentRequest(residentId: string, requestId: string): Promise<MaintenanceRequestDto> {
  return residentReqApi.get(residentId, requestId).then(res => res.data);
}

export function createResidentRequest(residentId: string, data: CreateRequestDto): Promise<MaintenanceRequestDto> {
  return residentReqApi.create2(residentId, data).then(res => res.data);
}

export function updateResidentRequest(residentId: string, requestId: string, data: UpdateRequestDto): Promise<MaintenanceRequestDto> {
  return residentReqApi.update4(residentId, requestId, data).then(res => res.data);
}

export function cancelResidentRequest(residentId: string, requestId: string): Promise<CancelResponseDto> {
  return residentReqApi.cancel(residentId, requestId).then(res => res.data);
}
