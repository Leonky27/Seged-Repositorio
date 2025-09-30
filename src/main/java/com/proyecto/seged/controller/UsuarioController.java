package com.proyecto.seged.controller;

import com.proyecto.seged.model.Usuario;
import com.proyecto.seged.service.UsuarioService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario){
        return new ResponseEntity<>(usuarioService.save(usuario), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios(){
        return new ResponseEntity<>(usuarioService.getUsuarios(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> get(@PathVariable ObjectId id){
        return new ResponseEntity<>(usuarioService.get(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable ObjectId id,@RequestBody Usuario usuario){
        return new ResponseEntity<>(usuarioService.update(id, usuario), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ObjectId id){
        usuarioService.delete(id);
        return new ResponseEntity<>("Usuario eliminado", HttpStatus.OK);
    }

}
