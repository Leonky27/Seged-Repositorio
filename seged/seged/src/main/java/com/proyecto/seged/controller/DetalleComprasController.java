package com.proyecto.seged.controller;

import com.proyecto.seged.model.DetalleCompras;
import com.proyecto.seged.service.DetalleComprasService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle_compras")
public class DetalleComprasController {

    @Autowired
    private DetalleComprasService detalleComprasService;

    @PostMapping
    public ResponseEntity<DetalleCompras> save(@RequestBody DetalleCompras detalleCompras) {
        return new ResponseEntity<>(detalleComprasService.save(detalleCompras), HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<DetalleCompras>> getDetalleCompras() {
        return new ResponseEntity<>(detalleComprasService.getDetalleCompras(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleCompras> get(@PathVariable String id) {
        return new ResponseEntity<>(detalleComprasService.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> detele(@PathVariable String id) {
        detalleComprasService.delete(id);
        return new ResponseEntity<>("Eliminación exitosa", HttpStatus.OK);
    }
}
