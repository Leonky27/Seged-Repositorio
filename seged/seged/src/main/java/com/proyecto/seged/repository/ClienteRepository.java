package com.proyecto.seged.repository;

import com.proyecto.seged.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, Integer> {

    Cliente findById(String id);

    Cliente findByNombre(String nombre);

    void deleteById(String id);
}
