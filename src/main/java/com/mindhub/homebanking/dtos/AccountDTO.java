package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    private Set<TransactionDTO> transactionDTOSet = new HashSet<>();

    public AccountDTO() {
    }

    public AccountDTO(Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactionDTOSet = new HashSet<>();
        for (Transaction transaction: account.getTransactions()){
            this.transactionDTOSet.add(new TransactionDTO(transaction));
        }
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactionDTOSet() {
        return transactionDTOSet;
    }
}
