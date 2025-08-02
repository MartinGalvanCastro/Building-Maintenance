import { ResidentialComplexManagementApi } from '../api/api';
import api, { apiBasePath } from '../utils/axios';
import type { ResidentialComplexDto, ResidentialComplexCommandDto, DeleteResponseDto } from '../api/api';

const rcApi = new ResidentialComplexManagementApi(undefined, apiBasePath, api);

export function getResidentialComplex(id: string): Promise<ResidentialComplexDto> {
  return rcApi.getOne2(id).then(res => res.data);
}

export function listResidentialComplexes(): Promise<ResidentialComplexDto[]> {
  return rcApi.listAll2().then(res => res.data);
}

export function createResidentialComplex(data: ResidentialComplexCommandDto): Promise<ResidentialComplexDto> {
  return rcApi.create3(data).then(res => res.data);
}

export function updateResidentialComplex(id: string, data: ResidentialComplexCommandDto): Promise<ResidentialComplexDto> {
  return rcApi.update2(id, data).then(res => res.data);
}

export function deleteResidentialComplex(id: string): Promise<DeleteResponseDto> {
  return rcApi.delete2(id).then(res => res.data);
}
