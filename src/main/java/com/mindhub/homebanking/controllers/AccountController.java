package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
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

    @GetMapping("/accounts")
    public List<AccountDTO> getClients() {
        return accountService.getAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getClient(@PathVariable Long id) {
        return accountService.findByIdDTO(id);
    }

    @GetMapping("/clients/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);
        if (client.getId() == account.getOwner().getId()) {
            return new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not your account.", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccounts(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount(Authentication authentication) {
        if (clientService.findByEmail(authentication.getName()).getAccounts().size() <= 2) {
            String accountNumber = generarNumeroSecuencial();
            Account newAccount = new Account(accountNumber, LocalDate.now(), 0, true);
            clientService.findByEmail(authentication.getName()).addAccount(newAccount);
            accountService.addAccount(newAccount);
        } else {
            return new ResponseEntity<>("No more accounts", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/accounts/deactivate")
    public ResponseEntity<Object> disableAccount(@RequestParam long id, Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);
        boolean existsAccount = client.getAccounts().contains(account);
        Set<Transaction> transactionSet = account.getTransactions();
        Set<Account> accounts = client.getAccounts();
        long accountActive = accounts.stream().filter(account1 -> account1.isActive()).count();

        if (account == null) {
            return new ResponseEntity<>("The account does not exist.", HttpStatus.FORBIDDEN);
        }
        if (existsAccount) {
            return new ResponseEntity<>("This account does not belong to you.", HttpStatus.FORBIDDEN);
        }
        if(accountActive <= 1){
            return new ResponseEntity<>("You cannot delete the only account you have.", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() > 0) {
            return new ResponseEntity<>("You cannot delete an account with a positive balance.", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance() < 0) {
            return new ResponseEntity<>("You cannot delete an account with a negative balance.", HttpStatus.FORBIDDEN);
        }
        transactionSet.forEach(transaction -> {
                    transaction.setActive(false);
                    transactionService.addTransaction(transaction);
                }
        );
        account.setActive(false);
        accountService.addAccount(account);
        return new ResponseEntity<>("This account was successfully deactivated.", HttpStatus.ACCEPTED);
    }
}
