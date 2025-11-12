package com.proyecto.seged.repository;

import com.proyecto.seged.model.Inventario;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventarioRepository extends MongoRepository<Inventario, ObjectId> {


    List<Inventario> findByActivoTrue();


    List<Inventario> findByProducto_Id(ObjectId productoId);


    List<Inventario> findByCategoria_Id(ObjectId categoriaId);
}

