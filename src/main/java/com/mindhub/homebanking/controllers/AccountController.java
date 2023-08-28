package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    private String randomNumber() {
        String random;
        do {
            int number = (int) (Math.random() * 99999999 + 1);
            random = "VIN-" + number;
        } while (accountRepository.findByNumber(random) != null);
        return random;
    }

    @RequestMapping("/accounts")
    public List<AccountDTO> getClients() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @RequestMapping("/clients/accounts/{id}")
    public ResponseEntity<Object>  getAccount(@PathVariable Long id, Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findById(id).orElse(null);
        if (client.getId() == account.getOwner().getId()){
            return new ResponseEntity<>(new AccountDTO(account),HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Not your account.", HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> newAccount(Authentication authentication) {
        if (clientRepository.findByEmail(authentication.getName()).getAccounts().size() <= 2) {
            String accountNumber = randomNumber();
            Account newAccount = new Account(accountNumber, LocalDate.now(), 0);
            clientRepository.findByEmail(authentication.getName()).addAccount(newAccount);
            accountRepository.save(newAccount);
        } else {
            return new ResponseEntity<>("No more accounts", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
