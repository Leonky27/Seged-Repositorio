import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? "http://localhost:8080",
  withCredentials: false,
});

const LOGIN_PATH = "/"; 

const handleUnauthorized = () => {
  sessionStorage.removeItem("token");

  if (window.location.pathname !== LOGIN_PATH) {
    window.location.replace(LOGIN_PATH);
  } else {
    window.location.reload();
  }
};

api.interceptors.request.use((config) => {
  const t = sessionStorage.getItem("token");
  if (t) {
    config.headers.Authorization = `Bearer ${t}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status;

    if (status === 401) {
      console.log("⚠️ 401 detectado, cerrando sesión y yendo al login");
      handleUnauthorized();
    }

    return Promise.reject(error);
  }
);

export default api;
