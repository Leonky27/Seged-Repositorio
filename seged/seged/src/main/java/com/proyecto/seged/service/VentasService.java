package com.proyecto.seged.service;

import com.proyecto.seged.model.Ventas;
import com.proyecto.seged.repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentasService {

    @Autowired
    private VentasRepository ventasRepository;

    public Ventas save(Ventas ventas) {
        return ventasRepository.save(ventas);
    }

    public List<Ventas> getVentas() {
        return ventasRepository.findAll();
    }

    public Ventas get(String id) {
        return ventasRepository.findById(id);
    }

    public void delete(String id) {
        ventasRepository.deleteById(id);
    }
}
