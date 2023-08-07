package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import java.util.HashSet;
import java.util.Set;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private Set<AccountDTO> accounts = new HashSet<>();
    public ClientDTO() {
    }
    public  ClientDTO(Client client) {

        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        this.accounts = new HashSet<>();
        for (Account account: client.getAccounts()) {
            this.accounts.add(new AccountDTO(account));
        }

    }
    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public ClientDTO(Set<AccountDTO> accounts) {
        this.accounts = accounts;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }
}
