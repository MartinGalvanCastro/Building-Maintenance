



import {
  MaintenanceRequestManagementApi,
  ResidentMaintenanceRequestsApi,
  TechnicianMaintenanceRequestsApi,
} from '@/api/api';
import api, { apiBasePath } from '@/utils/axios';
import type { CreateRequestDto, UpdateRequestDto, ChangeStatusDto } from '@/api/api';
import type { CreateMaintenanceRequestDto } from '@/api/api';
import type { RawAxiosRequestConfig } from 'axios';

const maintenanceRequestApi = new MaintenanceRequestManagementApi(undefined, apiBasePath, api);
const residentMaintenanceRequestApi = new ResidentMaintenanceRequestsApi(undefined, apiBasePath, api);
const technicianMaintenanceRequestApi = new TechnicianMaintenanceRequestsApi(undefined, apiBasePath, api);


// Admin endpoints

export const listMaintenanceRequestsAdmin = (options?: RawAxiosRequestConfig) => maintenanceRequestApi.listAll3(options).then(res => res.data);
export const getMaintenanceRequestAdmin = (id: string, options?: RawAxiosRequestConfig) => maintenanceRequestApi.getOne3(id, options).then(res => res.data);
export const deleteMaintenanceRequestAdmin = (id: string, options?: RawAxiosRequestConfig) => maintenanceRequestApi.delete3(id, options).then(res => res.data);
export const assignTechnicianToRequest = (id: string, technicianId: string, options?: RawAxiosRequestConfig) => maintenanceRequestApi.assign(id, technicianId, options).then(res => res.data);
export const createMaintenanceRequestAdmin = (data: CreateMaintenanceRequestDto, options?: RawAxiosRequestConfig) => maintenanceRequestApi.create4(data, options).then(res => res.data);
export const updateMaintenanceRequestAdmin = (id: string, data: UpdateRequestDto, options?: RawAxiosRequestConfig) => maintenanceRequestApi.update3(id, data, options).then(res => res.data);

// Resident endpoints
export const listMaintenanceRequestsResident = (residentId: string, options?: RawAxiosRequestConfig) => residentMaintenanceRequestApi.list(residentId, options).then(res => res.data);
export const getMaintenanceRequestResident = (residentId: string, requestId: string, options?: RawAxiosRequestConfig) => residentMaintenanceRequestApi.get(residentId, requestId, options).then(res => res.data);
export const createMaintenanceRequestResident = (residentId: string, data: CreateRequestDto, options?: RawAxiosRequestConfig) => residentMaintenanceRequestApi.create2(residentId, data, options).then(res => res.data);
export const updateMaintenanceRequestResident = (residentId: string, requestId: string, data: UpdateRequestDto, options?: RawAxiosRequestConfig) => residentMaintenanceRequestApi.update4(residentId, requestId, data, options).then(res => res.data);
export const deleteMaintenanceRequestResident = (residentId: string, requestId: string, options?: RawAxiosRequestConfig) => residentMaintenanceRequestApi.cancel(residentId, requestId, options).then(res => res.data);

// Technician endpoints
export const listMaintenanceRequestsTechnician = (technicianId: string, options?: RawAxiosRequestConfig) => technicianMaintenanceRequestApi.list1(technicianId, options).then(res => res.data);
export const changeMaintenanceRequestStatusTechnician = (technicianId: string, requestId: string, data: ChangeStatusDto, options?: RawAxiosRequestConfig) => technicianMaintenanceRequestApi.changeStatus(technicianId, requestId, data, options).then(res => res.data);
