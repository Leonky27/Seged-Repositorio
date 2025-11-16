export const Inicio = () => {
  return (
    <div className="d-flex align-items-center justify-content-center" style={{ minHeight: '80vh' }}>
      <div className="container px-4" style={{ transform: 'translateY(-24px)' }}>
        <div className="text-center mb-5">
          <h1 className="display-5 fw-bold mb-3">SEGED</h1>
          <p className="text-muted mb-0">
            Sistema de Gestión de Ventas para microempresas
          </p>
        </div>

        <div className="row g-4">
          <div className="col-12 col-lg-6">
            <div className="card h-100 shadow-sm border-0">
              <div className="card-body p-4">
                <h3 className="h4 fw-semibold mb-3">Misión</h3>
                <p className="mb-0">
                  Proveer a las microempresas una plataforma web de gestión de ventas, segura, intuitiva y accesible, que estandarice procesos, reduzca mermas y habilite decisiones basadas en datos para fortalecer su productividad y competitividad.
                </p>
              </div>
            </div>
          </div>

          <div className="col-12 col-lg-6">
            <div className="card h-100 shadow-sm border-0">
              <div className="card-body p-4">
                <h3 className="h4 fw-semibold mb-3">Visión</h3>
                <p className="mb-0">
                  Consolidarnos como la solución de referencia en gestión de ventas para microempresas en Latinoamérica, distinguiéndonos por innovación continua, confiabilidad operativa y un soporte cercano que impulse el crecimiento sostenible del tejido empresarial.
                </p>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>
  );
};
