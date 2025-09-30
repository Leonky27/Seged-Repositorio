package com.proyecto.seged.repository;

import com.proyecto.seged.model.Usuario;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, Integer> {

    Usuario findById(ObjectId id);

    void deleteById(ObjectId id);
}
