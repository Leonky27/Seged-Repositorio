package com.proyecto.seged.service;

import com.proyecto.seged.model.Usuario;
import com.proyecto.seged.repository.UsuarioRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario save(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> getUsuarios(){
        return usuarioRepository.findAll();
    }

    public Usuario get(ObjectId id){
        return usuarioRepository.findById(id);
    }

    public Usuario update(ObjectId id, Usuario usuario){
        Usuario usuarioActual = usuarioRepository.findById(id);
        usuarioActual.setNombre(usuario.getNombre());
        usuarioActual.setApellido(usuario.getApellido());
        usuarioActual.setCedula(usuario.getCedula());
        usuarioActual.setCelular(usuario.getCelular());
        usuarioActual.setEmail(usuario.getEmail());
        usuarioActual.setActivo(usuario.getActivo());

        return usuarioRepository.save(usuario);
    }

    public void delete(ObjectId id){
        usuarioRepository.deleteById(id);
    }
}
