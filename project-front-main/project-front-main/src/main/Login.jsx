import  { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export function Login() {
  const navigate = useNavigate();
  const { login } = useAuth(); 
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

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
    <>
      <style>{`
        .login-left {
          width: 65%;
        }
        .login-right {
          width: 35%;
        }
        @media (max-width: 767px) {
          .login-left {
            display: none;
          }
          .login-right {
            width: 100%;
          }
        }
      `}</style>
      
      <div className="d-flex" style={{ minHeight: '100vh' }}>
        {/* Área izquierda - 65% Hero Section */}
        <div 
          className="login-left"
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

        <div 
          className="login-right d-flex align-items-center justify-content-center p-4 bg-light"
        >
          <div className="w-100" style={{ maxWidth: '400px' }}>
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

                  <button type="submit" className="btn btn-dark w-100" disabled={loading}>
                    {loading ? "Ingresando..." : "Iniciar Sesión"}
                  </button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
