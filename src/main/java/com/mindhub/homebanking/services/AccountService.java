package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAccountsDTO();

    AccountDTO findByIdDTO(long id);

    Account findById(long id);

    void addAccount(Account account);

    Account findByNumber(String number);
}
