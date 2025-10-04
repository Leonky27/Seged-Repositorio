package com.proyecto.seged.controller;

import com.proyecto.seged.model.Client;
import com.proyecto.seged.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = {
        "http://127.0.0.1:5500",
        "http://localhost:5500"
}, allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<Client> save(@RequestBody Client client){
        return new ResponseEntity<>( clientService.save(client),  HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Client>> getUsuarios(){
        return new ResponseEntity<>( clientService.getUsuarios(),  HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> get(@PathVariable String id){
        return new ResponseEntity<>(clientService.get(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable String id, @RequestBody Client client){
        return new ResponseEntity<>(clientService.update(id, client), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        clientService.delete(id);
        return new ResponseEntity<>("usuario eliminado", HttpStatus.OK);
    }

}
