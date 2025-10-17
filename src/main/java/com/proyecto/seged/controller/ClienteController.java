package com.proyecto.seged.controller;

import com.proyecto.seged.model.Cliente;
import com.proyecto.seged.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = {
        "http://127.0.0.1:5500",
        "http://localhost:5500"
}, allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> save(@RequestBody Cliente cliente) {
        return new ResponseEntity<>(clienteService.save(cliente), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getUsuarios() {
        return new ResponseEntity<>(clienteService.getCliente(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> get(@PathVariable String id) {
        return new ResponseEntity<>(clienteService.get(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable String id, @RequestBody Cliente cliente) {
        return new ResponseEntity<>(clienteService.update(id, cliente), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        clienteService.delete(id);
        return new ResponseEntity<>("usuario eliminado", HttpStatus.OK);
    }

}
