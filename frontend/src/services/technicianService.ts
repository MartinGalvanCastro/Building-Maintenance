import { TechnicianManagementApi } from '../api/api';
import api, { apiBasePath } from '../utils/axios';
import type { TechnicianDto, TechnicianCreateCommandDto, TechnicianUpdateCommandDto, DeleteResponseDto } from '../api/api';

const techApi = new TechnicianManagementApi(undefined, apiBasePath, api);

export function getTechnician(id: string): Promise<TechnicianDto> {
  return techApi.getOne(id).then(res => res.data);
}

export function listTechnicians(): Promise<TechnicianDto[]> {
  return techApi.listAll().then(res => res.data);
}

export function createTechnician(data: TechnicianCreateCommandDto): Promise<TechnicianDto> {
  return techApi.create(data).then(res => res.data);
}

export function updateTechnician(id: string, data: TechnicianUpdateCommandDto): Promise<TechnicianDto> {
  return techApi.update(id, data).then(res => res.data);
}

export function deleteTechnician(id: string): Promise<DeleteResponseDto> {
  return techApi._delete(id).then(res => res.data);
}
