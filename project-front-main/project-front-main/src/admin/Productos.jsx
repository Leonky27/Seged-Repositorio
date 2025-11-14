import React, { useEffect, useState } from "react";
import { useProductos } from "../hooks/useProductos";
import api from "../api/client";

export function Productos() {
  const { items, loading, error, fetchAll, createOne, removeOne } = useProductos();

  const [categorias, setCategorias] = useState([]);
  const [formData, setFormData] = useState({
    nombre: "",
    descripcion: "",
    precioUnitario: "",
    categoriaId: "",
  });
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const loadCategorias = async () => {
      try {
        const res = await api.get("/api/categorias", { validateStatus: () => true });
        if (Array.isArray(res.data)) setCategorias(res.data);
        else if (Array.isArray(res.data?.content)) setCategorias(res.data.content);
        else setCategorias([]);
      } catch (e) {
        console.error("Error al cargar categorías", e);
      }
    };
    loadCategorias();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.nombre.trim()) return alert("El nombre es obligatorio");
    if (!formData.precioUnitario || Number(formData.precioUnitario) <= 0) {
      return alert("El precio debe ser mayor a 0");
    }

    setSubmitting(true);
    try {
      await createOne({
        nombre: formData.nombre,
        descripcion: formData.descripcion,
        precioUnitario: Number(formData.precioUnitario),
        categoriaId: formData.categoriaId || null,
      });
      setFormData({ nombre: "", descripcion: "", precioUnitario: "", categoriaId: "" });
    } catch (err) {
      alert(err.message || "Error al crear producto");
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id) => {
    if (!id) return;
    if (!window.confirm("¿Eliminar este producto?")) return;
    try {
      await removeOne(id);
    } catch (err) {
      alert(err.message || "No se pudo eliminar");
    }
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h3>Gestión de Productos</h3>
        <button className="btn btn-outline-secondary btn-sm" onClick={fetchAll} disabled={loading}>
          Recargar
        </button>
      </div>

      {/* Formulario */}
      <div className="card mb-4 shadow">
        <div className="card-header bg-primary text-white">
          <h5 className="mb-0">Nuevo Producto</h5>
        </div>
        <div className="card-body">
          <form onSubmit={handleSubmit} noValidate>
            <div className="row g-3">
              <div className="col-md-4">
                <label className="form-label">Nombre</label>
                <input
                  type="text"
                  name="nombre"
                  className="form-control"
                  value={formData.nombre}
                  onChange={handleChange}
                  disabled={submitting}
                  required
                />
              </div>
              <div className="col-md-4">
                <label className="form-label">Descripción</label>
                <input
                  type="text"
                  name="descripcion"
                  className="form-control"
                  value={formData.descripcion}
                  onChange={handleChange}
                  disabled={submitting}
                />
              </div>
              <div className="col-md-2">
                <label className="form-label">Precio Unitario</label>
                <input
                  type="number"
                  step="0.01"
                  min="0"
                  name="precioUnitario"
                  className="form-control"
                  value={formData.precioUnitario}
                  onChange={handleChange}
                  disabled={submitting}
                  required
                />
              </div>
              <div className="col-md-2">
                <label className="form-label">Categoría</label>
                <select
                  className="form-select"
                  name="categoriaId"
                  value={formData.categoriaId}
                  onChange={handleChange}
                  disabled={submitting}
                >
                  <option value="">Sin categoría</option>
                  {categorias.map((c) => (
                    <option key={c.id} value={c.id}>
                      {c.nombre}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="mt-3 d-flex justify-content-end">
              <button type="submit" className="btn btn-success" disabled={submitting}>
                {submitting ? "Guardando..." : "Guardar Producto"}
              </button>
            </div>
          </form>
        </div>
      </div>

      {/* Errores */}
      {error && (
        <div className="alert alert-danger">
          {error}
        </div>
      )}

      {/* Tabla */}
      <div className="card shadow">
        <div className="card-header bg-dark text-white">
          <h5 className="mb-0">Productos Registrados ({items.length})</h5>
        </div>
        <div className="card-body">
          {loading ? (
            <p className="text-center m-0">Cargando...</p>
          ) : items.length === 0 ? (
            <p className="text-center m-0">No hay productos registrados</p>
          ) : (
            <div className="table-responsive">
              <table className="table table-striped table-hover">
                <thead className="table-dark">
                  <tr>
                    <th>Nombre</th>
                    <th>Descripción</th>
                    <th>Precio Unitario</th>
                    <th>Categoría</th>
                    <th style={{ width: 100 }}>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {items.map((p) => (
                    <tr key={p.id}>
                      <td>{p.nombre}</td>
                      <td>{p.descripcion}</td>
                      <td>{p.precioUnitario.toFixed(2)}</td>
                      <td>{p.categoriaNombre || "-"}</td>
                      <td>
                        <button
                          className="btn btn-sm btn-danger"
                          onClick={() => handleDelete(p.id)}
                        >
                          Eliminar
                        </button>
                        {/* Si luego agregamos edición, aquí va el botón Editar */}
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
