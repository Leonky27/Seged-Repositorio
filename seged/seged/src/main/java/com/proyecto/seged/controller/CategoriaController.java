package com.proyecto.seged.controller;

import com.proyecto.seged.model.Categoria;
import com.proyecto.seged.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorías")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<Categoria> save(@RequestBody Categoria categoria) {
        return new ResponseEntity<>(categoriaService.save(categoria), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> getCategorias() {
        return new ResponseEntity<>(categoriaService.getCategoria(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> get(@PathVariable String id) {
        return new ResponseEntity<>(categoriaService.get(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        categoriaService.delete(id);
        return new ResponseEntity<>("Categoria eliminada con éxito", HttpStatus.OK);
    }
}
