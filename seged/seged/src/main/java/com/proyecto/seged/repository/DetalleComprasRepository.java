package com.proyecto.seged.repository;

import com.proyecto.seged.model.DetalleCompras;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleComprasRepository extends MongoRepository<DetalleCompras, Integer> {

    DetalleCompras findById(String id);

    void deleteById(String id);
}
