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
        clientActual.setApellido(client.getApellido());
        clientActual.setCedula(client.getCedula());
        clientActual.setCelular(client.getCelular());
        clientActual.setEmail(client.getEmail());
        clientActual.setActivo(client.getActivo());

        return clientRepository.save(client);
    }

    public void delete(String id){
        clientRepository.deleteById(id);
    }
}
