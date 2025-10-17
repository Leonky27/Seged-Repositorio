package com.proyecto.seged.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Setter
@Getter
@Document(collection = "Categorias")
public class Categoria {

    @Id
    private String id;

    private String nombre;

    private String descripcion;

    private Instant fecha_creacion;

}
