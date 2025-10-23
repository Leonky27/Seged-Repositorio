package com.proyecto.seged.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Document(collection = "Productos")
public class Producto {

    @Id

    private Date fechadecreacion;

    private String id;

    private String nombre;

    private String descripcion;

    private BigDecimal preciounitario; //ESTO MAPEA UN DECIMAL EN MONGITO
    @DBRef

    private Categoria categoria;



}
