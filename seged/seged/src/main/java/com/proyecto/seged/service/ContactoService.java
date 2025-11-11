package com.proyecto.seged.service;

import com.proyecto.seged.model.Contacto;
import com.proyecto.seged.repository.ContactoRepository;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class ContactoService {

    private final ContactoRepository contactoRepository;

    public ContactoService(ContactoRepository contactoRepository) {
        this.contactoRepository = contactoRepository;
    }


    public Contacto save(Contacto contacto) {
        if (contacto.getFecha_registro() == null) {
            contacto.setFecha_registro(new Date());
        }
        if (contacto.getActivo() == null) {
            contacto.setActivo(true);
        }
        return contactoRepository.save(contacto);
    }


    public List<Contacto> getContactos() {
        return contactoRepository.findAll();
    }

    public Contacto get(String id) {
        return contactoRepository.findById(id).orElse(null);
    }

    public void delete(String id) {
        contactoRepository.deleteById(id);
    }
}

