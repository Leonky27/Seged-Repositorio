package com.proyecto.seged.service;

import com.proyecto.seged.model.Client;
import com.proyecto.seged.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client save(Client client){
        return clientRepository.save(client);
    }

    public List<Client> getUsuarios(){
        return clientRepository.findAll();
    }

    public Client get(String id){
        return clientRepository.findById(id);
    }

    public Client update(String id, Client client){
        Client clientActual = clientRepository.findById(id);
        clientActual.setNombre(client.getNombre());
        clientActual.setCedula(client.getCedula());
        clientActual.setCorreo(client.getCorreo());
        clientActual.setCelular(client.getCelular());
        clientActual.setDireccion(client.getDireccion());

        return clientRepository.save(client);
    }

    public void delete(String id){
        clientRepository.deleteById(id);
    }
}
