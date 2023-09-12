package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    ClientService clientService;
    @Autowired
    LoanService loanService;
    @Autowired
    AccountService accountService;
    @Autowired
    ClientLoanService clientLoanService;
    @Autowired
    TransactionService transactionService;
    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getAllLoans();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> newLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {
        Client client = clientService.findByEmail(authentication.getName());
        if (client == null) {
            return new ResponseEntity<>("Client no found.", HttpStatus.FORBIDDEN);
        }
        Loan loan = loanService.findById(loanApplicationDTO.getId());
        if (loan == null) {
            return new ResponseEntity<>("The requested loan does not exist.", HttpStatus.FORBIDDEN);
        }
        if (client.getLoans().contains(loan.getName())){
            return new ResponseEntity<>("You can't ask for the same loan",HttpStatus.FORBIDDEN);
        }
        if (loan.getPayments() == null) {
            return new ResponseEntity<>("Number of payments not found.", HttpStatus.FORBIDDEN);
        }
        if (loan.getMaxAmount() <= 0) {
            return new ResponseEntity<>("The requested amount cannot be zero.", HttpStatus.FORBIDDEN);
        }
        if (loan.getMaxAmount() < loanApplicationDTO.getAmount()) {
            return new ResponseEntity<>("The amount requested cannot exist.", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("The chosen payment is not available.", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getNumberAccountDestination() == null) {
            return new ResponseEntity<>("Account destination no exist.",HttpStatus.FORBIDDEN);
        }
        Account account = accountService.findByNumber(loanApplicationDTO.getNumberAccountDestination());
        if (account == null) {
            return new ResponseEntity<>("Destination account does not exist.", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccounts().contains(account)){
            return new ResponseEntity<>("The account does not belong to an authenticated client",HttpStatus.FORBIDDEN);
        }
        if (account.getOwner().getId() != client.getId()) {
            return new ResponseEntity<>("This account does not belong to you.", HttpStatus.UNAUTHORIZED);
        }
        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * 1.2, loanApplicationDTO.getPayments());
        clientLoan.setClient(client);
        clientLoan.setLoan(loan);
        clientLoanService.addClientLoan(clientLoan);
        String transactionDescription = loan.getName() + "Loan approved.";
        Transaction transaction = new Transaction(loanApplicationDTO.getAmount(), transactionDescription, LocalDateTime.now(), TransactionType.CREDIT);
        transactionService.addTransaction(transaction);
        accountService.addAccount(account);
        account.addTransaction(transaction);
        return new ResponseEntity<>("Loan request created successfully", HttpStatus.CREATED);
    }
}
