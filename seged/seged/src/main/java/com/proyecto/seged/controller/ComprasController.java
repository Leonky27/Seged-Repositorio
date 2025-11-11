package com.proyecto.seged.controller;

import com.proyecto.seged.model.Compras;
import com.proyecto.seged.service.ComprasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {

    @Autowired
    private ComprasService comprasService;

    @PostMapping
    public ResponseEntity<Compras> save(@RequestBody Compras compras) {
        return new ResponseEntity<>(comprasService.save(compras), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Compras>> getCompras() {
        return new ResponseEntity<>(comprasService.getCompras(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compras> get(@PathVariable String id) {
        return new ResponseEntity<>(comprasService.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        comprasService.delete(id);
        return new ResponseEntity<>("Compra eliminada con éxito", HttpStatus.OK);
    }
}
