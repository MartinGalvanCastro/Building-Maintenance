import { ResidentManagementApi } from '../api/api';
import api, { apiBasePath } from '../utils/axios';
import type { ResidentDto, ResidentCreateCommandDto, ResidentUpdateCommandDto, DeleteResponseDto } from '../api/api';

const residentApi = new ResidentManagementApi(undefined, apiBasePath, api);

export function getResident(id: string): Promise<ResidentDto> {
  return residentApi.getOne1(id).then(res => res.data);
}

export function listResidents(): Promise<ResidentDto[]> {
  return residentApi.listAll1().then(res => res.data);
}

export function createResident(data: ResidentCreateCommandDto): Promise<ResidentDto> {
  return residentApi.create1(data).then(res => res.data);
}

export function updateResident(id: string, data: ResidentUpdateCommandDto): Promise<ResidentDto> {
  return residentApi.update1(id, data).then(res => res.data);
}

export function deleteResident(id: string): Promise<DeleteResponseDto> {
  return residentApi.delete1(id).then(res => res.data);
}
