package com.proyecto.seged.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "DetalleCompras")
public class DetalleCompras {

    @Id
    private String id;

    private String compra_id;

    private String producto_id;

    private int cantidad;

    private double precioUnitario;

    private Descuentos descuentos;

    private double subtotal;

    public static class Descuentos {

        private String tipo;
        private double valor;
    }
}
