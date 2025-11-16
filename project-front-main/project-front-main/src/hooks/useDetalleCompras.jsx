import { useCallback, useState } from "react";
import api from "../api/client";

export function useDetalleCompras() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const createDetalle = useCallback(async (payload) => {
    setLoading(true);
    setError(null);
    try {
      const body = {
        compra_id: payload.compraId,
        producto_id: payload.productoId,
        cantidad: payload.cantidad,
        precioUnitario: payload.precioUnitario,
        descuentoTipo: payload.descuentoTipo || "",
        descuentoValor: payload.descuentoValor || 0,
        subtotal: payload.subtotal,
      };

      const res = await api.post("/api/detalle_compras", body, { 
        validateStatus: () => true 
      });

      if (res.status >= 400) {
        throw new Error(res?.data?.message || "No se pudo crear el detalle");
      }

      return res.data;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  return { createDetalle, loading, error };
}
