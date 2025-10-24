import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../Auth/AuthContext";

export function Login() {
  const navigate = useNavigate();
  const { login } = useAuth(); 
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading]   = useState(false);
  const [error, setError]       = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError(null);
    setLoading(true);
    try {
      const token = await login(username, password);
      if (token) {
        setTimeout(() => {
          navigate("/inicio", { replace: true });
        }, 100);
      } else {
        setError("Error de autenticación");
      }
    } catch (e) {
      const msg = e?.response?.data?.message || "Usuario o contraseña inválidos";
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="row justify-content-center mt-5">
        <div className="col-12 col-md-6 col-lg-4">
          <div className="card shadow">
            <div className="card-body">
              <div className="row">
                <div className="d-flex justify-content-center align-items-baseline gap-2 text-center">
                  <h2 className="m-0">Iniciar</h2>
                  <h2 className="m-0">Sesión</h2>
                </div>
              </div>

              <form onSubmit={handleSubmit} noValidate>
                <div className="mb-3">
                  <label htmlFor="username" className="form-label">Usuario</label>
                  <input
                    type="text"
                    className="form-control"
                    id="username"
                    placeholder="Ingresa tu usuario"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    autoComplete="username"
                    required
                    disabled={loading}
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="password" className="form-label">Contraseña</label>
                  <input
                    type="password"
                    className="form-control"
                    id="password"
                    placeholder="Ingresa tu contraseña"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    autoComplete="current-password"
                    required
                    disabled={loading}
                  />
                </div>

                {error && (
                  <div className="alert alert-danger py-2" role="alert">
                    {error}
                  </div>
                )}

                <button type="submit" className="btn btn-primary w-100" disabled={loading}>
                  {loading ? "Ingresando..." : "Iniciar Sesión"}
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
