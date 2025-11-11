package com.proyecto.seged.repository;

import com.proyecto.seged.model.Contacto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

    public interface ContactoRepository extends MongoRepository<Contacto, String> {


        List<Contacto> findByActivoTrue();

        List<Contacto> findByTipoIgnoreCase(String tipo);

        List<Contacto> findByNombreContainingIgnoreCase(String nombre);

        Optional<Contacto> findByIdentificacion(String identificacion);
    }


