import { UserInfoApi } from '@/api/api';
import api, { apiBasePath } from '@/utils/axios';
import type { RawAxiosRequestConfig } from 'axios';

const userInfoApi = new UserInfoApi(undefined, apiBasePath, api);

export const getCurrentUser = (options?: RawAxiosRequestConfig) => userInfoApi.getCurrentUser(options).then(res => res.data);
