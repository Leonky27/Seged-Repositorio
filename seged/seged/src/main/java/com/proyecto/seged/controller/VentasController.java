package com.proyecto.seged.controller;

import com.proyecto.seged.model.Ventas;
import com.proyecto.seged.service.VentasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentasController {

    @Autowired
    private VentasService ventasService;

    @PostMapping
    public ResponseEntity<Ventas> save(@RequestBody Ventas ventas) {
        return new ResponseEntity<>(ventasService.save(ventas), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Ventas>> getVentas() {
        return new ResponseEntity<>(ventasService.getVentas(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ventas> get(@PathVariable String id) {
        return new ResponseEntity<>(ventasService.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        ventasService.delete(id);
        return new ResponseEntity<>("Venta eliminada con éxito", HttpStatus.OK);
    }
}
