package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

public class LoanDTO {
    private long id;
    private String name;

    public LoanDTO() {
    }

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
