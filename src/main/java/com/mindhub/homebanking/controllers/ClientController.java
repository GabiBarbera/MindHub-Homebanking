package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientService clientService;

    private int numeroSecuencial = 0;

    private String generarNumeroSecuencial() {
        String numeroFormateado;
        boolean numeroDuplicado;
        do {
            numeroSecuencial++;
            numeroFormateado = String.format("%08d", numeroSecuencial);
            numeroDuplicado = accountRepository.findByNumber("VIN-" + numeroFormateado) != null;
            if (numeroSecuencial >= 99999999) {
                numeroSecuencial = 0;
            }
        } while (numeroDuplicado);
        return "VIN-" + numeroFormateado;
    }

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.getClientsDTO();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        return clientService.getClientDTO(authentication.getName());
    }

    @PostMapping("/clients")
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
        if (!email.matches("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$")) {
            return new ResponseEntity<>("Incorrect email format.", HttpStatus.FORBIDDEN);
        }
        if (password.isBlank()) {
            return new ResponseEntity<>("The password is missing, please enter it.", HttpStatus.FORBIDDEN);
        }
        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            return new ResponseEntity<>("The password must contain at least 8 characters, a capital letter, a number and a special character!", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.addClient(newClient);
        String accountNumber = generarNumeroSecuencial();
        Account newAccount = new Account(accountNumber, LocalDate.now(), 0, true, AccountType.SAVINGS);
        newClient.addAccount(newAccount);
        accountRepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}