package com.proyecto.seged.repository;

import com.proyecto.seged.model.Categoria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends MongoRepository<Categoria, Integer> {

    Categoria findById(String id);

    void deleteById(String id);

}
