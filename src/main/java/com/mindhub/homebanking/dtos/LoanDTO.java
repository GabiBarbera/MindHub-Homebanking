package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoanDTO {
    private long id;
    private String name;
    private Double maxAmount;
    private List<Integer> payments;
    private Set<ClientLoan> clientLoans = new HashSet<>();

}
