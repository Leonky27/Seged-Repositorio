import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../auth/authContext";

export function Register() {
  const navigate = useNavigate();
  const { register } = useAuth();

  const [form, setForm] = useState({
    username: "",
    password: "",
    confirm: "",
    // Si tu backend también pide email, descomenta:
    // email: ""
  });
  const [showPass, setShowPass] = useState(false);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState(null);

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const onSubmit = async (e) => {
    e.preventDefault();
    setErr(null);

    if (!form.username.trim()) return setErr("El usuario es obligatorio.");
    if (form.password !== form.confirm) return setErr("Las contraseñas no coinciden.");

    setLoading(true);
    try {
      // Ajusta el payload si tu backend requiere otros campos
      await register(form.username, form.password /* , form.email */);
      navigate("/clientes", { replace: true });
    } catch (e) {
      const msg = e?.response?.data?.message || "No se pudo registrar. Intenta de nuevo.";
      setErr(msg);
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
              <h2 className="text-center mb-3">Crear cuenta</h2>

              {err && <div className="alert alert-danger py-2">{err}</div>}

              <form onSubmit={onSubmit} noValidate>
                <div className="mb-3">
                  <label htmlFor="username" className="form-label">Usuario</label>
                  <input
                    id="username"
                    name="username"
                    className="form-control"
                    value={form.username}
                    onChange={onChange}
                    autoComplete="username"
                    disabled={loading}
                    required
                  />
                </div>

                {/* Si tu backend usa email, descomenta
                <div className="mb-3">
                  <label htmlFor="email" className="form-label">Correo</label>
                  <input
                    type="email"
                    id="email"
                    name="email"
                    className="form-control"
                    value={form.email}
                    onChange={onChange}
                    autoComplete="email"
                    disabled={loading}
                    required
                  />
                </div>
                */}

                <div className="mb-3">
                  <label htmlFor="password" className="form-label">Contraseña</label>
                  <div className="input-group">
                    <input
                      type={showPass ? "text" : "password"}
                      id="password"
                      name="password"
                      className="form-control"
                      value={form.password}
                      onChange={onChange}
                      autoComplete="new-password"
                      disabled={loading}
                      required
                    />
                    <button
                      type="button"
                      className="btn btn-outline-secondary"
                      onClick={() => setShowPass((s) => !s)}
                      tabIndex={-1}
                    >
                      {showPass ? "Ocultar" : "Ver"}
                    </button>
                  </div>
                </div>

                <div className="mb-3">
                  <label htmlFor="confirm" className="form-label">Confirmar contraseña</label>
                  <input
                    type={showPass ? "text" : "password"}
                    id="confirm"
                    name="confirm"
                    className="form-control"
                    value={form.confirm}
                    onChange={onChange}
                    autoComplete="new-password"
                    disabled={loading}
                    required
                  />
                </div>

                <button type="submit" className="btn btn-primary w-100" disabled={loading}>
                  {loading ? "Creando..." : "Crear cuenta"}
                </button>
              </form>

              <div className="text-center mt-3">
                <small>
                  ¿Ya tienes cuenta? <Link to="/login">Inicia sesión</Link>
                </small>
              </div>
            </div>
          </div>

          <p className="text-muted text-center mt-3" style={{ fontSize: 12 }}>
            Al registrarte aceptas los términos y condiciones.
          </p>
        </div>
      </div>
    </div>
  );
}
