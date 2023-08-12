package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;

public class ClientLoanDTO {
    private long id;
    private Double amount;
    private Integer payments;
    private Client client;
    private Loan loan;
}
