package com.proyecto.seged.controller;

import com.proyecto.seged.model.Contacto;
import com.proyecto.seged.service.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contactos")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;


    @PostMapping
    public ResponseEntity<Contacto> save(@RequestBody Contacto contacto) {
        return new ResponseEntity<>(contactoService.save(contacto), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<Contacto>> getContactos() {
        return new ResponseEntity<>(contactoService.getContactos(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Contacto> get(@PathVariable String id) {
        return new ResponseEntity<>(contactoService.get(id), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        contactoService.delete(id);
        return new ResponseEntity<>("Eliminación exitosa", HttpStatus.OK);
    }
}


