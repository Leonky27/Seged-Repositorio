package com.proyecto.seged.service;

import com.proyecto.seged.model.Categoria;
import com.proyecto.seged.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria save(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> getCategoria(){
        return categoriaRepository.findAll();
    }

    public Categoria get(String id){
        return categoriaRepository.findById(id);
    }

}
