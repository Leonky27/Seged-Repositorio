import React, { useState } from "react";
import { Link } from "react-router-dom";

export function Register() {
  const [form, setForm] = useState({
    username: "",
    password: "",
    confirm: ""
  });
  const [showPass, setShowPass] = useState(false);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState(null);

  const onChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value
    });
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    setErr(null);
    
    if (form.password !== form.confirm) {
      setErr("Las contraseñas no coinciden");
      return;
    }
    
    setLoading(true);
    try {
      // Aquí va tu lógica de registro
      console.log("Datos del formulario:", form);
    } catch (error) {
      setErr("Error al crear la cuenta");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <style>{`
        .register-left {
          width: 65%;
        }
        .register-right {
          width: 35%;
        }
        @media (max-width: 767px) {
          .register-left {
            display: none;
          }
          .register-right {
            width: 100%;
          }
        }
      `}</style>
      
      <div className="d-flex" style={{ minHeight: '100vh' }}>
        {/* Área izquierda - 65% Hero Section */}
        <div 
          className="register-left"
          style={{ 
            background: 'linear-gradient(135deg, rgba(0, 123, 255, 0.8) 0%, rgba(0, 0, 0, 0.7) 100%)',
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            backgroundRepeat: 'no-repeat',
            position: 'relative'
          }}
        >
          <div 
            style={{
              position: 'absolute',
              top: 0,
              left: 0,
              right: 0,
              bottom: 0,
              backgroundColor: 'rgba(0, 0, 0, 0.5)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center'
            }}
          >
            <div className="text-white px-5" style={{ maxWidth: '600px' }}>
              <h1 className="display-3 fw-bold mb-4">SEGED</h1>
              <h2 className="h3 mb-4">Sistema de Gestión de Inventario</h2>
              <p className="lead mb-4">
                La solución perfecta para microempresas que buscan optimizar 
                el control de su inventario de manera simple y eficiente.
              </p>
              <p className="text-white-50">
                Gestiona tu inventario de forma profesional sin complicaciones. 
                SEGED te ayuda a tomar decisiones informadas sobre tu stock.
              </p>
            </div>
          </div>
        </div>

        {/* Área derecha - 35% Registro */}
        <div 
          className="register-right d-flex align-items-center justify-content-center p-4 bg-light"
        >
          <div className="w-100" style={{ maxWidth: '400px' }}>
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

                  <button type="submit" className="btn btn-dark w-100" disabled={loading}>
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
          </div>
        </div>
      </div>
    </>
  );
}
