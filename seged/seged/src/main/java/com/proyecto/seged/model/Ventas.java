package com.proyecto.seged.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "Ventas")
public class Ventas {

    @Id
    private String id;

    private String cliente_id;

    private String usuario_id;

    private InformacionVenta informacionVenta;

    private Calculos calculos;

    private String estado;

    private Instant fechaCreacion;

    private Instant fechaActualizacion;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class InformacionVenta {

        private String numero;
        private Instant fecha;
        private String metodoPago;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Calculos {

        private int subTotal;
        private int impuestos;
        private int total;

    }

}
