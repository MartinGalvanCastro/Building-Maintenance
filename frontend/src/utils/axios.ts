import axios from 'axios';


export const apiBasePath = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
const api = axios.create({
  baseURL: apiBasePath,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers = config.headers || {};
    config.headers['Authorization'] = `Bearer ${token}`;
  }
  return config;
});

export default api;
