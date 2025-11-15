import { useMemo, useState } from "react";
import { useVentas } from "../hooks/useVentas";
import { useDetalleVentas } from "../hooks/useDetalleVentas";
import { useClientes } from "../hooks/useClientes";
import { useProductos } from "../hooks/useProductos";
import { Link } from "react-router-dom";
import workerApi from "../api/workerClient";
import api from "../api/client";  

export function Ventas() {
  const { 
    items: ventas, 
    loading, 
    error, 
    createVenta, 
    createVentaConInventario,  
    removeVenta 
  } = useVentas();
  
  const { createDetalle } = useDetalleVentas();
  const { items: clientes } = useClientes();
  const { items: productos } = useProductos();

  const [ventaForm, setVentaForm] = useState({
    clienteId: "",
    numero: "",
    metodoPago: "Efectivo",
  });

  const [lineForm, setLineForm] = useState({
    productoId: "",
    productoNombre: "",
    cantidad: 1,
    precioUnitario: 0,
    descuentoTipo: "",
    descuentoValor: 0,
  });

  const [lineas, setLineas] = useState([]);

  const onChangeVenta = (e) => {
    const { name, value } = e.target;
    setVentaForm((prev) => ({ ...prev, [name]: value }));
  };

  const onChangeLinea = (e) => {
    const { name, value } = e.target;

    if (name === "productoId") {
      const selected = productos.find((p) => p.id === value);

      setLineForm((prev) => ({
        ...prev,
        productoId: value,
        productoNombre: selected ? selected.nombre : "",
        precioUnitario: selected ? selected.precioUnitario : 0,
      }));
      return;
    }

    setLineForm((prev) => ({
      ...prev,
      [name]:
        name === "cantidad" || name === "precioUnitario" || name === "descuentoValor"
          ? Number(value)
          : value,
    }));
  };

  const addLinea = () => {
    if (!lineForm.productoId.trim()) {
      return alert("Debes seleccionar un producto.");
    }
    if (!lineForm.cantidad || lineForm.cantidad <= 0) {
      return alert("Cantidad inválida.");
    }

    const subtotalLinea =
      lineForm.cantidad * lineForm.precioUnitario - (lineForm.descuentoValor || 0);

    setLineas((prev) => [
      ...prev,
      {
        ...lineForm,
        id: typeof crypto !== "undefined" && crypto.randomUUID
          ? crypto.randomUUID()
          : String(Date.now() + Math.random()),
        subtotal: subtotalLinea,
      },
    ]);

    setLineForm((prev) => ({
      ...prev,
      cantidad: 1,
      descuentoTipo: "",
      descuentoValor: 0,
    }));
  };

  const removeLineaLocal = (id) => {
    setLineas((prev) => prev.filter((l) => l.id !== id));
  };

  const totales = useMemo(() => {
    const subTotal = lineas.reduce((acc, l) => acc + l.subtotal, 0);
    const impuestos = 0;
    const total = subTotal + impuestos;
    return { subTotal, impuestos, total };
  }, [lineas]);

  const onSubmitVenta = async (e) => {
    e.preventDefault();

    if (!ventaForm.clienteId.trim()) {
      return alert("Debes seleccionar un cliente.");
    }

    let lineasParaVenta = lineas;

    if (lineasParaVenta.length === 0) {
      if (!lineForm.productoId.trim()) {
        return alert("Agrega al menos un producto a la venta.");
      }
      if (!lineForm.cantidad || lineForm.cantidad <= 0) {
        return alert("Cantidad inválida para el producto.");
      }

      const subtotalLinea =
        lineForm.cantidad * lineForm.precioUnitario - (lineForm.descuentoValor || 0);

      lineasParaVenta = [
        {
          ...lineForm,
          id:
            typeof crypto !== "undefined" && crypto.randomUUID
              ? crypto.randomUUID()
              : String(Date.now() + Math.random()),
          subtotal: subtotalLinea,
        },
      ];
    }

    const subTotal = lineasParaVenta.reduce((acc, l) => acc + l.subtotal, 0);
    const impuestos = 0;
    const total = subTotal + impuestos;

    const now = new Date().toISOString();

    const payloadVenta = {
      cliente_id: ventaForm.clienteId,
      usuario_id: null,
      informacionVenta: {
        numero: ventaForm.numero || "VENTA-" + Date.now(),
        fecha: now,
        metodoPago: ventaForm.metodoPago,
      },
      calculos: {
        subTotal,
        impuestos,
        total,
      },
      estado: "REGISTRADA",
      fechaCreacion: now,
      fechaActualizacion: now,
    };

    try {
      const ventaCreada = await createVentaConInventario(payloadVenta, lineasParaVenta);

      for (const linea of lineasParaVenta) {
        await createDetalle({
          ventaId: ventaCreada.id,
          productoId: linea.productoId,
          cantidad: linea.cantidad,
          precioUnitario: linea.precioUnitario,
          descuentoTipo: linea.descuentoTipo,
          descuentoValor: linea.descuentoValor,
          subtotal: linea.subtotal,
        });
      }

      try {
        const sqlPayload = buildSqlVentaPayload(payloadVenta, lineasParaVenta);
        const resSql = await workerApi.post("/api/ventas", sqlPayload, {
          validateStatus: () => true,
        });

        if (resSql.status >= 400) {
          console.error(
            "Error desde backend SQL:",
            resSql.status,
            resSql.data
          );
        } else {
          console.log("Venta registrada también en SQL:", resSql.data);
        }
      } catch (errSql) {
        console.error("Error de red al llamar al backend SQL:", errSql);
      }

      alert("Venta registrada con éxito");
      setVentaForm({ clienteId: "", numero: "", metodoPago: "Efectivo" });
      setLineForm({
        productoId: "",
        productoNombre: "",
        cantidad: 1,
        precioUnitario: 0,
        descuentoTipo: "",
        descuentoValor: 0,
      });
      setLineas([]);
    } catch (err) {
      console.error(err);
      alert(err.message || "No se pudo registrar la venta.");
    }
  };

  const handleDeleteVenta = async (id) => {
    const venta = ventas.find((v) => v.id === id);

    if (!venta) {
      return alert("No se encontró la venta en memoria.");
    }

    if (
      !window.confirm(
        `¿Eliminar la venta ${venta.numero || id}? Esto también eliminará sus detalles.`
      )
    ) {
      return;
    }

    try {
      try {
        const resDet = await api.get("/api/detalle_ventas", {
          validateStatus: () => true,
        });

        if (resDet.status < 400) {
          const data = Array.isArray(resDet.data)
            ? resDet.data
            : resDet.data?.content ?? [];

          const detallesDeEstaVenta = data.filter(
            (d) => d.venta_id === id || d.ventaId === id
          );

          await Promise.all(
            detallesDeEstaVenta.map((d) =>
              api.delete(`/api/detalle_ventas/${d.id}`, {
                validateStatus: () => true,
              })
            )
          );
        } else {
          console.error(
            "Error obteniendo detalle_ventas para eliminar:",
            resDet.status,
            resDet.data
          );
        }
      } catch (errDet) {
        console.error("Error eliminando detalle_ventas en Mongo:", errDet);
      }

      await removeVenta(id);

      if (venta.numero) {
        try {
          const resSql = await workerApi.delete(
            `/api/ventas/numero/${encodeURIComponent(venta.numero)}`,
            { validateStatus: () => true }
          );

          if (resSql.status >= 400 && resSql.status !== 404) {
            console.error(
              "Error eliminando venta en SQL:",
              resSql.status,
              resSql.data
            );
          }
        } catch (errSql) {
          console.error("Error de red al eliminar venta en SQL:", errSql);
        }
      }

      alert("Venta eliminada correctamente.");
    } catch (err) {
      console.error(err);
      alert(err.message || "No se pudo eliminar la venta.");
    }
  };

  const getClienteNombre = (id) => {
    const found = clientes.find((c) => c.id === id);
    return found ? found.nombre : id;
  };

  const buildSqlVentaPayload = (ventaPayload, lineasParaVenta) => {
    const cliente = clientes.find((c) => c.id === ventaPayload.cliente_id);

    const detalles = lineasParaVenta.map((l) => {
      const prod = productos.find((p) => p.id === l.productoId);
      const descuentoNum = Number(l.descuentoValor || 0);

      return {
        productoId: null,
        nombreProducto: prod?.nombre || l.productoNombre,
        cantidad: l.cantidad,
        precioUnitario: l.precioUnitario,
        descuento: descuentoNum,
        subtotal: l.subtotal,
      };
    });

    return {
      cliente: cliente
        ? {
            clienteId: null,
            nombre: cliente.nombre,
            docId: cliente.cedula || null,
          }
        : null,

      usuario: null,

      informacionVenta: {
        numero: ventaPayload.informacionVenta.numero,
        fecha: ventaPayload.informacionVenta.fecha,
        metodoPago: ventaPayload.informacionVenta.metodoPago,
        tipo: "NORMAL",
      },

      calculos: {
        subtotal: ventaPayload.calculos.subTotal,
        impuestos: ventaPayload.calculos.impuestos,
        total: ventaPayload.calculos.total,
      },

      detalles,
    };
  };

  return (
    <div className="container mt-4">
      <h3 className="mb-3">Gestión de Ventas</h3>

      <div className="card mb-4 shadow">
        <div className="card-header bg-primary text-white">
          <h5 className="mb-0">Nueva Venta</h5>
        </div>
        <div className="card-body">
          <form onSubmit={onSubmitVenta}>
            <div className="row g-3 mb-3">
              <div className="col-md-4">
                <label className="form-label">Cliente</label>
                <select
                  name="clienteId"
                  className="form-select"
                  value={ventaForm.clienteId}
                  onChange={onChangeVenta}
                  required
                >
                  <option value="">Seleccione un cliente</option>
                  {clientes.map((c) => (
                    <option key={c.id} value={c.id}>
                      {c.nombre} — {c.cedula}
                    </option>
                  ))}
                </select>
              </div>

              <div className="col-md-4">
                <label className="form-label">Número de venta</label>
                <input
                  name="numero"
                  className="form-control"
                  value={ventaForm.numero}
                  onChange={onChangeVenta}
                  placeholder="Opcional, se genera si lo dejas vacío"
                />
              </div>

              <div className="col-md-4">
                <label className="form-label">Método de pago</label>
                <select
                  name="metodoPago"
                  className="form-select"
                  value={ventaForm.metodoPago}
                  onChange={onChangeVenta}
                >
                  <option value="Efectivo">Efectivo</option>
                  <option value="Tarjeta">Tarjeta</option>
                  <option value="Transferencia">Transferencia</option>
                </select>
              </div>
            </div>

            <h6>Detalle de productos</h6>
            <div className="row g-2 align-items-end mb-2">
              <div className="col-md-3">
                <label className="form-label">Producto</label>
                <select
                  name="productoId"
                  className="form-select"
                  value={lineForm.productoId}
                  onChange={onChangeLinea}
                  required
                >
                  <option value="">Seleccione un producto</option>
                  {productos.map((p) => (
                    <option key={p.id} value={p.id}>
                      {p.nombre} — ${p.precioUnitario}
                    </option>
                  ))}
                </select>
              </div>
              <div className="col-md-2">
                <label className="form-label">Cantidad</label>
                <input
                  type="number"
                  name="cantidad"
                  className="form-control"
                  min={1}
                  value={lineForm.cantidad}
                  onChange={onChangeLinea}
                />
              </div>
              <div className="col-md-2">
                <label className="form-label">Precio Unit.</label>
                <input
                  type="number"
                  name="precioUnitario"
                  className="form-control"
                  min={0}
                  step="0.01"
                  value={lineForm.precioUnitario}
                  onChange={onChangeLinea}
                />
              </div>
              <div className="col-md-2">
                <label className="form-label">Desc. tipo</label>
                <input
                  name="descuentoTipo"
                  className="form-control"
                  value={lineForm.descuentoTipo}
                  onChange={onChangeLinea}
                  placeholder="% o fijo"
                />
              </div>
              <div className="col-md-2">
                <label className="form-label">Desc. valor</label>
                <input
                  type="number"
                  name="descuentoValor"
                  className="form-control"
                  min={0}
                  step="0.01"
                  value={lineForm.descuentoValor}
                  onChange={onChangeLinea}
                />
              </div>
              <div className="col-md-1">
                <button type="button" className="btn btn-success w-100" onClick={addLinea}>
                  +
                </button>
              </div>
            </div>

            {lineas.length > 0 && (
              <div className="table-responsive mb-3">
                <table className="table table-sm table-striped">
                  <thead>
                    <tr>
                      <th>Producto</th>
                      <th>Cant.</th>
                      <th>P.Unit</th>
                      <th>Desc.</th>
                      <th>Subtotal</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {lineas.map((l) => (
                      <tr key={l.id}>
                        <td>{l.productoNombre || l.productoId}</td>
                        <td>{l.cantidad}</td>
                        <td>{l.precioUnitario.toFixed(2)}</td>
                        <td>{l.descuentoValor.toFixed(2)}</td>
                        <td>{l.subtotal.toFixed(2)}</td>
                        <td>
                          <button
                            type="button"
                            className="btn btn-sm btn-danger"
                            onClick={() => removeLineaLocal(l.id)}
                          >
                            X
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}

            <div className="d-flex justify-content-between align-items-center mb-2">
              <div>
                <strong>Subtotal:</strong> {totales.subTotal.toFixed(2)} &nbsp;
                <strong>Impuestos:</strong> {totales.impuestos.toFixed(2)} &nbsp;
                <strong>Total:</strong> {totales.total.toFixed(2)}
              </div>
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? "Guardando..." : "Registrar Venta"}
              </button>
            </div>
          </form>
        </div>
      </div>

      <div className="card shadow">
        <div className="card-header bg-dark text-white">
          <h5 className="mb-0">Ventas registradas ({ventas.length})</h5>
        </div>
        <div className="card-body">
          {error && <div className="alert alert-danger">{error}</div>}
          {loading && <p className="text-center m-0">Cargando...</p>}

          {!loading && ventas.length === 0 && (
            <p className="text-center m-0">No hay ventas registradas.</p>
          )}

          {!loading && ventas.length > 0 && (
            <div className="table-responsive">
              <table className="table table-striped table-hover">
                <thead className="table-dark">
                  <tr>
                    <th>Número</th>
                    <th>Cliente (id)</th>
                    <th>Método Pago</th>
                    <th>Total</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {ventas.map((v) => (
                    <tr key={v.id}>
                      <td>{v.numero}</td>
                      <td>{getClienteNombre(v.clienteId)}</td>
                      <td>{v.metodoPago}</td>
                      <td>{v.total.toFixed(2)}</td>
                      <td>{v.estado}</td>
                      <td className="d-flex gap-2">
                        <Link to={`/ventas/${v.id}`} className="btn btn-sm btn-outline-primary">
                          Ver detalle
                        </Link>
                        <button
                          className="btn btn-sm btn-danger"
                          onClick={() => handleDeleteVenta(v.id)}
                        >
                          Eliminar
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
