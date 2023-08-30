package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
   private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(path = "/transactions" ,method = RequestMethod.POST)
    public ResponseEntity<Object>transaction(@RequestParam double amount, @RequestParam String description, @RequestParam String accountOrigin, @RequestParam String accountDestiny, Authentication authentication){
        if (amount <= 0){
            return new ResponseEntity<>("Insufficient amount to carry out the transaction.", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank()){
            return new ResponseEntity<>("There is no description of the transaction, please write one.", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin.isBlank()){
            return new ResponseEntity<>("There is no source account, please select one.", HttpStatus.FORBIDDEN);
        }
        if (accountDestiny.isBlank()){
            return new ResponseEntity<>("There is no destination account, please select one.", HttpStatus.FORBIDDEN);
        }
        if (accountOrigin.equals(accountDestiny)){
            return new ResponseEntity<>("The source and destination accounts cannot be the same.", HttpStatus.FORBIDDEN);
        }
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = client.getAccounts().stream().filter(account1 -> account1.getNumber().equals(accountOrigin)).findFirst().orElse(null);
        Account accountDes = accountRepository.findByNumber(accountDestiny);
        if (account.getBalance() >= amount){
            account.setBalance(account.getBalance() - amount);
            accountDes.setBalance(accountDes.getBalance() + amount);
            Transaction transactionDebit = new Transaction(amount, description, LocalDateTime.now(), TransactionType.DEBIT);
            Transaction transactionCredit = new Transaction(amount, description, LocalDateTime.now(), TransactionType.CREDIT);
            account.addTransaction(transactionDebit);
            accountDes.addTransaction(transactionCredit);
            accountRepository.save(account);
            accountRepository.save(accountDes);
            transactionRepository.save(transactionDebit);
            transactionRepository.save(transactionCredit);
        }else {
            return new ResponseEntity<>("Insufficient balance.", HttpStatus.FORBIDDEN);
        }
       return new ResponseEntity<>("The transaction was successful!",HttpStatus.CREATED);
    }
}
