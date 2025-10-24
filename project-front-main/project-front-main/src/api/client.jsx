import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? "http://localhost:8080",
  withCredentials: false
});

api.interceptors.request.use((config) => {
  const t = sessionStorage.getItem("token"); 
  if (t) config.headers.Authorization = `Bearer ${t}`;
  return config;
});

api.interceptors.response.use(
  (r) => r,
  (err) => {
    const status = err?.response?.status;
    if (status === 401) {
      sessionStorage.removeItem("token");
      window.location.href = "/login";
    }
    return Promise.reject(err);
  }
);

export default api;
