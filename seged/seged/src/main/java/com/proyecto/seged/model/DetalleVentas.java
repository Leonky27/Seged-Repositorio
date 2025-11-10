package com.proyecto.seged.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "DetalleVentas")
public class DetalleVentas {

    @Id
    private String id;

    private String venta_id;

    private String producto_id;

    private int cantidad;

    private int precio_unitario;

    private Descuento descuento;

    private int subtotal;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Descuento {

        private String tipo;
        private int valor;
    }
}
