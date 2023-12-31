package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    List<LoanDTO> getAllLoans();

    Loan findById(long id);

    Loan findByName(String name);

    Loan addLoan(Loan loan);
}
