package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> transaction(@RequestParam Double amount, @RequestParam String description, @RequestParam String accountOrigin, @RequestParam String accountDestiny, Authentication authentication) {
        if (amount <= 0) {
            return new ResponseEntity<>("Insufficient amount to carry out the transaction.", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank()) {
            return new ResponseEntity<>("There is no description of the transaction, please write one.", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin.isBlank()) {
            return new ResponseEntity<>("There is no source account, please select one.", HttpStatus.FORBIDDEN);
        }
        if (accountDestiny.isBlank()) {
            return new ResponseEntity<>("There is no destination account, please select one.", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin.equals(accountDestiny)) {
            return new ResponseEntity<>("The source and destination accounts cannot be the same.", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(authentication.getName());
        Account account = client.getAccounts().stream().filter(account1 -> account1.getNumber().equals(accountOrigin)).findFirst().orElse(null);
        Account accountDes = accountService.findByNumber(accountDestiny);
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            accountDes.setBalance(accountDes.getBalance() + amount);
            Transaction transactionDebit = new Transaction(amount * -1, description, LocalDateTime.now(), TransactionType.DEBIT, account.getBalance(), true);
            Transaction transactionCredit = new Transaction(amount, description, LocalDateTime.now(), TransactionType.CREDIT, accountDes.getBalance(), true);
            account.addTransaction(transactionDebit);
            accountDes.addTransaction(transactionCredit);
            accountService.addAccount(account);
            accountService.addAccount(accountDes);
            transactionService.addTransaction(transactionDebit);
            transactionService.addTransaction(transactionCredit);
        } else {
            return new ResponseEntity<>("Insufficient balance.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("The transaction was successful!", HttpStatus.CREATED);
    }
}
