package com.proyecto.seged.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "inventario")
public class inventario {
    @Getter
    @Setter
    @Id
    private String id;
    private String productoId;
    private InformacionProducto informacionProducto;
    private int stockActual;
    private int stockMinimo;
    private int stockMaximo;
    private List<Movimiento> movimientos;
    private Date fechaUltimaActualizacion;
    private boolean activo;

    public static class InformacionProducto {
        private String nombre;
        private String categoriaid;

    }

    public static class Movimiento {
        private Date fechaMovimiento;
        private String tipoMovimiento;
        private int cantidad;
        private String motivo;
        private String usuarioId;
        private String referenciaFactura;

        private int stockNuevo;
    }
}
