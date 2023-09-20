package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getClientsDTO();

    void addClient(Client client);

    Client findByEmail(String email);

    ClientDTO getClientDTO(String email);

    ClientDTO findById(long id);
    Client findByIdNoDTO(long id);
}
