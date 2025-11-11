package com.proyecto.seged.service;

import com.proyecto.seged.model.Compras;
import com.proyecto.seged.repository.ComprasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComprasService {

    @Autowired
    private ComprasRepository comprasRepository;

    public Compras save(Compras compras) {
        return comprasRepository.save(compras);
    }

    public List<Compras> getCompras() {
        return comprasRepository.findAll();
    }

    public Compras get(String id) {
        return comprasRepository.findById(id);
    }

    public void delete(String id) {
        comprasRepository.deleteById(id);
    }
}
