package com.mindhub.homebanking.dtos;

import java.util.List;

public class LoanApplicationDTO {
 private long id;
 private double amount;
 private Integer payments;
 private String numberAccountDestination;
 private double interest;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(long id, double amount, Integer payments, String numberAccountDestination, double interest) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.numberAccountDestination = numberAccountDestination;
        this.interest = interest;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getNumberAccountDestination() {
        return numberAccountDestination;
    }

    public double getInterest() {
        return interest;
    }
}
