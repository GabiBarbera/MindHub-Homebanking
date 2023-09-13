package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository  clientRepository;
    @Autowired
    TransactionRepository transactionRepository;
    private List<Loan> loans = loanRepository.findAll();
    private List<Account> accounts = accountRepository.findAll();
   private List<Card> cards = cardRepository.findAll();
   private List<Client> clients = clientRepository.findAll();
   private List<Transaction> transactions = transactionRepository.findAll();

    @Test
    public void existsLoans() {
        assertThat(loans, is(not(empty())));
    }

    @Test
    public void existsPersonalLoan() {
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void existsAccount() {
        assertThat(accounts,notNullValue());
    }

    @Test
    public void positiveBalance(){
        assertThat(accounts,hasItem(hasProperty("balance",greaterThanOrEqualTo(0.0))));
    }

    @Test
    public void existsCard() {
        assertThat(cards, notNullValue());
    }

    @Test
    public void threeDigitsCvvCard() {
        assertThat(cards, hasItem(hasProperty("cvv", isA(Integer.class))));
    }

    @Test
    public void quantityClient() {
        assertThat(clients, hasSize(greaterThanOrEqualTo(3)));
    }

    @Test
    public void correctEmail(){
        assertThat(clients, hasItem(hasProperty("email", stringContainsInOrder( "@",".com"))));
    }

    @Test
    public void positiveAmount(){
        assertThat(transactions,hasItem(hasProperty("amount",greaterThanOrEqualTo(0.0))));
    }

    @Test
    public void existsTransactions(){
        assertThat(transactions, notNullValue());
    }
}