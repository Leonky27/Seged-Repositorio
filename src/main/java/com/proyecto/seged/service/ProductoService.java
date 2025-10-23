package com.proyecto.seged.service;


import com.proyecto.seged.model.Producto;
import com.proyecto.seged.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository ProductoRepository;

    public Producto save(Producto Producto){
        return ProductoRepository.save(Producto);
    }
    public List<Producto> getProductos(){
        return ProductoRepository.findAll();
    }
}
