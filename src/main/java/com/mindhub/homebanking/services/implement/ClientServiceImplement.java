package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<ClientDTO> getClientsDTO() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @Override
    public void addClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public ClientDTO getClientDTO(String email) {
        return new ClientDTO(this.findByEmail(email));
    }

    @Override
    public ClientDTO findById(long id) {
        return new ClientDTO(clientRepository.findById(id).orElse(null));
    }

    @Override
    public Client findByIdNoDTO(long id) {
        return clientRepository.findById(id).orElse(null);
    }


}
