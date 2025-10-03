package com.proyecto.seged.repository;

import com.proyecto.seged.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends MongoRepository<Client, Integer> {

    Client findById(String id);

    void deleteById(String id);
}
