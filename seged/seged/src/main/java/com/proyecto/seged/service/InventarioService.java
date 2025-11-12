package com.proyecto.seged.service;

import com.proyecto.seged.model.Inventario;
import com.proyecto.seged.repository.InventarioRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;


    public Inventario save(Inventario inventario) {
        if (inventario.getFechaUltimaActualizacion() == null) {
            inventario.setFechaUltimaActualizacion(new Date());
        }
        if (inventario.getActivo() == null) {
            inventario.setActivo(true);
        }
        return inventarioRepository.save(inventario);
    }


    public List<Inventario> getInventarios() {
        return inventarioRepository.findAll();
    }


    public Inventario get(String id) {
        if (!ObjectId.isValid(id)) return null;
        return inventarioRepository.findById(new ObjectId(id)).orElse(null);
    }


    public void delete(String id) {
        if (!ObjectId.isValid(id)) return;
        inventarioRepository.deleteById(new ObjectId(id));
    }


    public Inventario registrarMovimiento(String inventarioId,
                                          String tipoMovimiento,
                                          Double cantidad,
                                          String motivo,
                                          ObjectId usuarioId,
                                          ObjectId ventaId,
                                          ObjectId compraId) {

        if (!ObjectId.isValid(inventarioId)) {
            throw new IllegalArgumentException("ID de inventario inválido");
        }
        if (cantidad == null || cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        Inventario inv = inventarioRepository.findById(new ObjectId(inventarioId))
                .orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado"));

        double stockAnterior = inv.getStockActual() == null ? 0d : inv.getStockActual();
        double stockNuevo;

        switch (tipoMovimiento == null ? "" : tipoMovimiento.toLowerCase()) {
            case "entrada" -> stockNuevo = stockAnterior + cantidad;
            case "salida"  -> stockNuevo = Math.max(0d, stockAnterior - cantidad);
            case "ajuste"  -> stockNuevo = Math.max(0d, cantidad);
            default -> throw new IllegalArgumentException("Tipo de movimiento no soportado");
        }

        // Actualiza el inventario  alvaro99
        inv.setStockActual(stockNuevo);
        inv.setFechaUltimaActualizacion(new Date());

        // Construye movimiento y lo agrega pampara99
        Inventario.Movimiento mov = new Inventario.Movimiento();
        mov.setFechaMovimiento(new Date());
        mov.setTipoMovimiento(tipoMovimiento);
        mov.setCantidad(cantidad);
        mov.setMotivo(motivo);
        mov.setUsuarioId(usuarioId);
        mov.setVentaId(ventaId);
        mov.setCompraId(compraId);
        mov.setStockAnterior(stockAnterior);
        mov.setStockNuevo(stockNuevo);

        if (inv.getMovimientos() != null) {
            inv.getMovimientos().add(mov);
        }

        return inventarioRepository.save(inv);
    }
}

