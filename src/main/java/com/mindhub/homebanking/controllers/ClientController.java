package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;

    private String randomNumber(){
        String random;
        do {
            int number = (int)(Math.random() * 99999999 + 1);
            random = "VIN-" + number;
        } while (accountRepository.findByNumber(random)!=null);
        return random;
    }

    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @RequestMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isBlank()) {
            return new ResponseEntity<>("The name is missing, please enter it.", HttpStatus.FORBIDDEN);
        }
        if (lastName.isBlank()) {
            return new ResponseEntity<>("The last name is missing, please enter it.", HttpStatus.FORBIDDEN);
        }
        if (email.isBlank()) {
            return new ResponseEntity<>("The email is missing, please enter it.", HttpStatus.FORBIDDEN);
        }
        if (password.isBlank()) {
            return new ResponseEntity<>("The password is missing, please enter it.", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(newClient);
        String accountNumber = randomNumber();
        Account newAccount = new Account(accountNumber, LocalDate.now(),0);
        newClient.addAccount(newAccount);
        accountRepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}